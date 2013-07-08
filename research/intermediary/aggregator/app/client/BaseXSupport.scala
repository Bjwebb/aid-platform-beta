/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 25/06/2013
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */

package client

trait BaseXSupport {

  implicit class BetterizedQuery(client: BaseXClient) {

    private val XQ_PATH = "/Users/james/Projects/dfid/aid-platform-beta/research/basex/tracker/xq"

    def run(filename: String) = {
      client.execute(s"RUN $XQ_PATH/$filename.xq")
    }
  }

  def withSession[T](action: BaseXClient => T) = {
    val client = BaseXClient("localhost", 1984, "admin", "admin")
    try {
      action(client)
    } finally {
      client.close()
    }
  }
}
