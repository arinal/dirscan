package commons

trait Entity {
  val id: Int

   override def equals(other: Any): Boolean = other match {
     case that: Entity => id == that.id
     case _ => false
   }

   override def hashCode: Int = id
}
