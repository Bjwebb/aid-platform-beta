package uk.gov.dfid.common.models

import reactivemongo.bson.{BSONString, BSONDocument, BSONObjectID}
import reactivemongo.bson.handlers.{BSONWriter, BSONReader}

case class Project(
  id: Option[BSONObjectID],
  iatiId: String,
  title: String,
  description: String,
  projectType: String)

object Project {

  implicit object ProjectReader extends BSONReader[Project]{
    def fromBSON(doc: BSONDocument): Project = {
      val document = doc.toTraversable

      Project(
        document.getAs[BSONObjectID]("_id"),
        document.getAs[BSONString]("iatiId").map(_.value).get,
        document.getAs[BSONString]("title").map(_.value).get,
        document.getAs[BSONString]("description").map(_.value).get,
        document.getAs[BSONString]("projectType").map(_.value).get
      )
    }
  }

  implicit object ProjectWriter extends BSONWriter[Project]{
    def toBSON(project: Project): BSONDocument = {
      BSONDocument(
        "_id"         -> project.id.getOrElse(BSONObjectID.generate),
        "iatiId"      -> BSONString(project.iatiId),
        "title"       -> BSONString(project.title),
        "description" -> BSONString(project.description),
        "projectType" -> BSONString(project.projectType)
      )
    }
  }
}