package com.vixxx123.scalasprayslickexample.rest

import spray.json.DefaultJsonProtocol._

import scala.slick.driver.MySQLDriver.simple._

package object person {

  val TableName = "person"

  case class Person(id: Option[Int] = None, name: String, lastname: String)

  class PersonT(tag: Tag) extends Table[Person](tag, TableName) {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def name: Column[String] = column[String]("name")
    def lastname: Column[String] = column[String]("lastname")

    def * = (id.?, name, lastname) <> (
      (Person.apply _).tupled, Person.unapply)
  }

  implicit val UserFormat = jsonFormat3(Person)

  implicit val DeleteFormat = jsonFormat1(DeleteResult)

  val Persons: TableQuery[PersonT] = TableQuery[PersonT]

  val PersonsIdReturning = Persons returning Persons.map{_.id}
}