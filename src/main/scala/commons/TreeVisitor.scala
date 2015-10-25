package commons

import scala.annotation.tailrec

object TreeVisitor {
  def traverse[A](fringe: List[A])(implicit canFanout: A => Boolean, fanout: A => List[A]) = {
    @tailrec
    def recurse(fringe: List[A], acc: List[A]): List[A] = {
      fringe match {
        case List() => acc
        case head :: rest =>
          if (canFanout(head)) recurse(fanout(head) ::: rest, head :: acc)
          else recurse(rest, head :: acc)
      }
    }
    recurse(fringe.toList, List())
  }

  def traverse[A](fringe: A*)(implicit canFanout: A => Boolean, fanout: A => List[A]): List[A] = traverse(fringe.toList)
}
