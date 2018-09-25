package it.polimi.genomics.core.DAG

import it.polimi.genomics.core.DataStructures.IRDataSet

class ExecutionDAG(val dag: List[OperatorDAG], deps: List[ExecutionDAG]) extends DAGNode[ExecutionDAG] {
  /** Returns the list of dependencies of the node */
  override def getDependencies: List[ExecutionDAG] = this.deps

  /** A list of the source datasets which are used by this node */
  override def sources: Set[IRDataSet] = throw new NotImplementedError()

  override def substituteDependency(oldDep: ExecutionDAG, newDep: ExecutionDAG): ExecutionDAG =
    throw new NotImplementedError()
}