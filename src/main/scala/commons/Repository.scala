package commons

trait Repository[A <: Entity] {
  def byId(id: Int): Option[A]
  def all: List[A]
  def save(entity: A)
  def delete(id: Int)

  def reconstruct()
}
