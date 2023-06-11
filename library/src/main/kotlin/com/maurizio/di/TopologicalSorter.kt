package com.maurizio.di

class TopologicalSorter {

    fun doTopologicalSorting(
        outgoingEdges: Map<Class<out Any>, List<Class<*>>>,
        incomingEdges: Map<Class<out Any>, Int>
    ) {
        val inDegrees = incomingEdges.toMutableMap()

        var nodesWithZeroInDegrees = computeNodesWithZeroInDegreesAndUpdate(inDegrees)

        while (nodesWithZeroInDegrees.isNotEmpty()) {
            nodesWithZeroInDegrees.forEach { node ->
                updateInDegreesOfConnectedNodes(node, outgoingEdges, inDegrees)
            }
            nodesWithZeroInDegrees = computeNodesWithZeroInDegreesAndUpdate(inDegrees)
        }

        if (inDegrees.isNotEmpty()) {
            throw CircularDependencyException("Circular dependency detected")
        }
    }

    private fun computeNodesWithZeroInDegreesAndUpdate(inDegrees: MutableMap<Class<out Any>, Int>): List<Class<out Any>> {
        val nodesWitZeroInDegrees = inDegrees.filter { it.value == 0 }.map { it.key }
        nodesWitZeroInDegrees.forEach { inDegrees.remove(it) }
        return nodesWitZeroInDegrees
    }

    private fun updateInDegreesOfConnectedNodes(
        node: Class<out Any>,
        outgoingEdges: Map<Class<out Any>, List<Class<*>>>,
        inDegrees: MutableMap<Class<out Any>, Int>
    ) {
        val outgoing = requireNotNull(node.declaredConstructors.first()).declaringClass
        outgoingEdges[outgoing]?.forEach { outgoingNode ->
            inDegrees[outgoingNode] = requireNotNull(inDegrees[outgoingNode]) - 1
        }
    }
}
