import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.app.Connection
import ca.uhn.hl7v2.app.Initiator
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.Parser

fun main() {
    val context: HapiContext = DefaultHapiContext()
    val parser: Parser = context.pipeParser

    val admissionsMsg = ("MSH|^~\\&|HIS|RIH|EKG|EKG|199904140038||ADT^A01|12345|P|2.2\r"
            + "PID|0001|00009874|00001122|A00977|SMITH^JOHN^M|MOM|19581119|F|NOTREAL^LINDA^M|C|564 SPRING ST^^NEEDHAM^MA^02494^US|0002|(818)565-1551|(425)828-3344|E|S|C|0000444444|252-00-4414||||SA|||SA||||NONE|V1|0001|I|D.ER^50A^M110^01|ER|P00055|11B^M011^02|070615^BATMAN^GEORGE^L|555888^NOTREAL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^NOTREAL^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|199904101200||||5555112333|||666097^NOTREAL^MANNY^P\r"
            + "NK1|0222555|NOTREAL^JAMES^R|FA|STREET^OTHER STREET^CITY^ST^55566|(222)111-3333|(888)999-0000|||||||ORGANIZATION\r"
            + "PV1|0001|I|D.ER^1F^M950^01|ER|P000998|11B^M011^02|070615^BATMAN^GEORGE^L|555888^OKNEL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^VOICE^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|||||5555112333|||666097^DNOTREAL^MANNY^P\r"
            + "PV2|||0112^TESTING|55555^PATIENT IS NORMAL|NONE|||19990225|19990226|1|1|TESTING|555888^NOTREAL^BOB^K^DR^MD||||||||||PROD^003^099|02|ER||NONE|19990225|19990223|19990316|NONE\r"
            + "AL1||SEV|001^POLLEN\r"
            + "GT1||0222PL|NOTREAL^BOB^B||STREET^OTHER STREET^CITY^ST^77787|(444)999-3333|(222)777-5555||||MO|111-33-5555||||NOTREAL GILL N|STREET^OTHER STREET^CITY^ST^99999|(111)222-3333\r"
            + "IN1||022254P|4558PD|BLUE CROSS|STREET^OTHER STREET^CITY^ST^00990||(333)333-6666||221K|LENIX|||19980515|19990515|||PATIENT01 TEST D||||||||||||||||||02LL|022LP554")

    val labReportMessage =
            "MSH|^~\\&|FORMENTRY|AMRS|HL7LISTENER|AMRS|20050217152845||ORU^R01|AMRS20050217152845|P|2.5\r" +
            "PID||1^\\^^AMRS|1MT^9^M10||Patient^Jonny^Dee{^}{^}DR|Patient^Momma^Thee^\\^MS|20040101000000^Y|M||B|555 Johnson Road^Apt.555^Indianapolis^IN^46202^USA|||||||||||Indianapolis, IN|||||||||||||||||TRIBE CODE\r" +
            "PV1|1|O|^^^^^^^^^1^AMRS|2|||1^Mamlin^Joseph^^^^^^^^8^M10^^AMRS|||||||||||||||||||||||||||||||||||||20050217140000|||||||V\r" +
            "ORC|RE||||||||20050221130000|1^Enterer^Ima^^^^^AMRS\r" +
            "OBX|2|SN|5497^CD4 COUNT^DCT||<^10|cells/mm3|10-1500|L|||F|||20050217204000\r"

    val adt: Message = parser.parse(labReportMessage)

    println("Starting Client")

    val connection: Connection = context.newClient(Config.server, Config.port, Config.useTls)
    val initiator: Initiator = connection.initiator
    var response: Message = initiator.sendAndReceive(adt)

    val responseString: String = parser.encode(response)
    println("Received response:\n$responseString")
//    connection = context.newClient(Config.server, Config.port, Config.useTls)
//    initiator = connection.initiator
//    response = initiator.sendAndReceive(adt)
//    println("->$response")
    connection.close()
}