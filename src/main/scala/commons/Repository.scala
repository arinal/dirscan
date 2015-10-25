package commons

trait Repository[A <: Entity] {
  def length: Int
  def byId(id: Int): Option[A]
  def all: List[A]
  def save(entity: A)
  def delete(id: Int)
}
