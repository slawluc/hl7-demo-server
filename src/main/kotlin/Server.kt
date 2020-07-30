import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.app.Connection
import ca.uhn.hl7v2.app.ConnectionListener
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.protocol.ReceivingApplication
import ca.uhn.hl7v2.protocol.ReceivingApplicationExceptionHandler
import java.io.IOException


/**
 * http://dev.patientsknowbest.com/home/data-model#TOC-Lab-Report
 * http://dev.patientsknowbest.com/home/hl7-api/messages/oru-r01/laboratory
 * https://hapifhir.github.io/hapi-hl7v2/v281/apidocs/ca/uhn/hl7v2/model/v281/message/ORU_R01.html
 * https://hapifhir.github.io/hapi-hl7v2/v281/apidocs/index.html
 * https://wiki.openmrs.org/display/docs/HL7
 */
fun main() {
    println("Starting")
    val handler: ReceivingApplication<Message> = ExampleReceiverApplication()

    DefaultHapiContext().newServer(Config.port, Config.useTls).apply {
        registerApplication("ADT", "A01", handler)
        registerApplication("ADT", "A02", handler)
        registerApplication("ORU", "R01", handler)
        registerConnectionListener(MyConnectionListener())
        setExceptionHandler(MyExceptionHandler())
        startAndWait()
    }
}

class ExampleReceiverApplication : ReceivingApplication<Message> {

    override fun canProcess(theIn: Message): Boolean = true

    @Throws(HL7Exception::class)
    override fun processMessage(theMessage: Message, theMetadata: Map<String, Any>): Message {
        val encodedMessage: String = DefaultHapiContext().pipeParser.encode(theMessage)
        println("Received message:\n$encodedMessage\n\n")
        return try {
            theMessage.generateACK()
        } catch (e: IOException) {
            throw HL7Exception(e)
        }
    }
}

class MyConnectionListener : ConnectionListener {
    override fun connectionReceived(theC: Connection) {
        println("New connection received: " + theC.remoteAddress.toString())
    }

    override fun connectionDiscarded(theC: Connection) {
        println("Lost connection from: " + theC.remoteAddress.toString())
    }
}

class MyExceptionHandler : ReceivingApplicationExceptionHandler {

    override fun processException(
        theIncomingMessage: String?,
        theIncomingMetadata: Map<String?, Any?>?,
        theOutgoingMessage: String,
        theE: Exception?
    ): String = theOutgoingMessage
}