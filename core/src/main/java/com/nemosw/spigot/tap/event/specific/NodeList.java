package com.nemosw.spigot.tap.event.specific;

/**
 * @author Nemo
 */
final class NodeList<E>
{
    private Node<E> first;
    private Node<E> last;

    public void add(E item)
    {
        Node<E> node = new Node<>(item);

        if (first == null)
        {
            first = last = node;
        }
        else
        {
            last = last.next = node;
        }
    }

    public void remove(E item)
    {
        Node<E> node = first;
        Node<E> prev = null;

        while (node != null)
        {
            if (item.equals(node.item))
            {
                node.item = null;

                if (prev == null)
                    first = node.next;
                else
                    prev.next = node.next;

                if (node == last)

            }

            prev = node;
            node = node.next;
        }
    }

    private static class Node<E>
    {
        E item;

        Node<E> next;

        public Node(E item)
        {
            this.item = item;
        }
    }
}
