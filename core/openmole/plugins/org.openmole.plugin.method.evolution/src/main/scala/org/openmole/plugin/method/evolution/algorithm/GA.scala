/*
 * Copyright (C) 2012 reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openmole.plugin.method.evolution.algorithm

import fr.iscpif.mgo._
import fr.iscpif.mgo.tools.Lazy
import java.util.Random

object GA {

  trait GA extends G with MG with MF with RankDiversityMF with GASigma {
    type MF <: Rank with Diversity
    type RANKED = MGFitness
    type DIVERSIFIED = MGFitness
    val gManifest = manifest[G]
    val individualManifest = manifest[Individual[G, F]]
    val populationManifest = manifest[Population[G, F, MF]]
    val fManifest = manifest[F]
  }

  trait GATermination extends Termination with TerminationManifest with MG with GA

  def counter(_steps: Int) =
    new CounterTermination with GATermination {
      val steps = _steps
      val stateManifest = manifest[STATE]
    }

  def timed(_duration: Long) =
    new TimedTermination with GATermination {
      val duration = _duration
      val stateManifest = manifest[STATE]
    }

  trait GARanking extends Ranking with GA

  trait GARankingBuilder {
    def apply(dominance: Dominance): GARanking
  }

  def pareto = new GARankingBuilder {
    def apply(_dominance: Dominance) = new ParetoRanking with GARanking {
      def isDominated(p1: Seq[Double], p2: Seq[Double]) = _dominance.isDominated(p1, p2)
    }
  }

  trait GADiversityMetric extends DiversityMetric with GA

  trait DiversityMetricBuilder {
    def apply(dominance: Dominance): GADiversityMetric
  }

  def crowding = new DiversityMetricBuilder {
    def apply(dominance: Dominance) = new CrowdingDiversity with GADiversityMetric
  }

  def hypervolume(_referencePoint: Seq[Double]) = new DiversityMetricBuilder {
    def apply(dominance: Dominance) = new HypervolumeDiversity with GADiversityMetric {
      def isDominated(p1: Seq[Double], p2: Seq[Double]) = dominance.isDominated(p1, p2)
      val referencePoint = _referencePoint
    }
  }

  def epsilon(_epsilons: Seq[Double]) = new EpsilonDominance {
    val epsilons = _epsilons
  }

  def strict = new StrictDominance {}
  def nonStrict = new NonStrictDominance {}

  trait GAModifier extends Modifier with GA
  trait GAArchive extends Archive with ArchiveManifest with GA with GAModifier {
    val diversityMetric: GADiversityMetric
    val ranking: GARanking
    def diversity(individuals: Seq[DIVERSIFIED], ranks: Seq[Lazy[Int]]): Seq[Lazy[Double]] = diversityMetric.diversity(individuals, ranks)
    def rank(individuals: Seq[RANKED]) = ranking.rank(individuals)

  }

  trait GAArchiveBuilder extends A {
    def apply(diversityMetric: GADiversityMetric, ranking: GARanking): GAArchive
  }

  def noArchive = new GAArchiveBuilder {
    def apply(_diversityMetric: GADiversityMetric, _ranking: GARanking) =
      new NoArchive with RankDiversityModifier with GAArchive {
        override type DIVERSIFIED = MGFitness
        override type RANKED = MGFitness
        val aManifest = manifest[A]
        val diversityMetric = _diversityMetric
        val ranking = _ranking
      }
  }

  def mapArchive(_plotter: GAPlotter, _aggregation: GAAggregation, _neighbors: Int = 8) =
    new GAArchiveBuilder {
      def apply(_diversityMetric: GADiversityMetric, _ranking: GARanking) =
        new MapArchive with GAArchive with MapModifier {
          override type DIVERSIFIED = MGFitness
          override type RANKED = MGFitness
          val aManifest = manifest[A]
          def plot(i: Individual[G, F]) = _plotter.plot(i)
          def aggregate(fitness: F) = _aggregation.aggregate(fitness)
          val diversityMetric = _diversityMetric
          val ranking = _ranking
          val neighbors = _neighbors
        }
    }

  trait GAAggregation extends Aggregation with MG
  trait GAPlotter extends Plotter with GA with MG

  def max = new MaxAggregation with GAAggregation {}

  def genomePlotter(_x: Int, _y: Int, _nX: Int = 100, _nY: Int = 100) = new GenomePlotter with GAPlotter {
    val x = _x
    val y = _y
    val nX = _nX
    val nY = _nY
  }

  trait GACrossover extends CrossOver with GA

  trait GACrossoverBuilder {
    def apply(genomeSize: Factory[GA#G]): GACrossover
  }

  def sbx(_distributionIndex: Double = 2.0) = new GACrossoverBuilder {
    def apply(_genomeFactory: Factory[GA#G]) =
      new SBXBoundedCrossover with GACrossover {
        val distributionIndex = _distributionIndex
        val genomeFactory = _genomeFactory
      }
  }

  trait GAMutation extends Mutation with GA

  trait GAMutationBuilder extends GA {
    def apply(genomeFactory: Factory[GA#G]): GAMutation
  }

  def coEvolvingSigma = new GAMutationBuilder {
    def apply(_genomeFactory: Factory[GA#G]) = new CoEvolvingSigmaValuesMutation with GAMutation {
      val genomeFactory = _genomeFactory
    }
  }

  def apply(
    mu: Int,
    lambda: Int,
    termination: GATermination,
    mutation: GAMutationBuilder = coEvolvingSigma,
    crossover: GACrossoverBuilder = sbx(),
    dominance: Dominance = strict,
    diversityMetric: DiversityMetricBuilder = crowding,
    ranking: GARankingBuilder = pareto,
    archiving: GAArchiveBuilder = noArchive) =
    new org.openmole.plugin.method.evolution.algorithm.GA(mu, lambda, termination, mutation, crossover, dominance, diversityMetric, ranking, archiving)(_)

}

sealed class GA(
  val mu: Int,
  val lambda: Int,
  val termination: GA.GATermination,
  val mutation: GA.GAMutationBuilder = GA.coEvolvingSigma,
  val crossover: GA.GACrossoverBuilder = GA.sbx(),
  val dominance: Dominance = GA.strict,
  val diversityMetric: GA.DiversityMetricBuilder = GA.crowding,
  val ranking: GA.GARankingBuilder = GA.pareto,
  val archiving: GA.GAArchiveBuilder = GA.noArchive)(val genomeSize: Int)
    extends MuPlusLambda
    with GASigmaFactory
    with BinaryTournamentSelection
    with NonDominatedElitism
    with EvolutionManifest
    with TerminationManifest
    with GA.GA
    with Archive
    with Termination
    with Breeding
    with MG
    with Elitism
    with Modifier
    with CloneRemoval { sga ⇒

  lazy val thisRanking = ranking(dominance)
  lazy val thisDiversityMetric = diversityMetric(dominance)
  lazy val thisArchiving = archiving(thisDiversityMetric, thisRanking)
  lazy val thisCrossover = crossover(genomeFactory)
  lazy val thisMutation = mutation(genomeFactory)

  type STATE = termination.STATE
  type A = thisArchiving.A

  implicit val aManifest = thisArchiving.aManifest

  implicit val stateManifest = termination.stateManifest

  def initialState(p: Population[G, F, MF]): STATE = termination.initialState(p)
  def terminated(population: Population[G, F, MF], terminationState: STATE): (Boolean, STATE) = termination.terminated(population, terminationState)
  def diversity(individuals: Seq[DIVERSIFIED], ranks: Seq[Lazy[Int]]): Seq[Lazy[Double]] = thisDiversityMetric.diversity(individuals, ranks)
  def isDominated(p1: Seq[scala.Double], p2: Seq[scala.Double]) = dominance.isDominated(p1, p2)
  def toArchive(individuals: Seq[Individual[G, F]]) = thisArchiving.toArchive(individuals)
  def combine(a1: A, a2: A) = thisArchiving.combine(a1, a2)
  def initialArchive = thisArchiving.initialArchive
  def modify(individuals: Seq[Individual[G, F]], archive: A) = thisArchiving.modify(individuals, archive)
  def crossover(g1: G, g2: G)(implicit aprng: Random) = thisCrossover.crossover(g1, g2)
  def mutate(genome: G)(implicit aprng: Random) = thisMutation.mutate(genome)
}