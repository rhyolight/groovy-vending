/*
 * author: Matthew Taylor
 */
class ConcurrencyTests extends GroovyTestCase {

    def vendor

    void setUp() {
        vendor = new VendingMachine()
        def inventory = [
                A1: [item:[name:'Baby Ruth'], price: 1.0, quantity: 1000],
        ]
        vendor.inventory = inventory
    }

    void testConcurrentAccess() {
        def runner = {
            10.times { i ->
                println "${Thread.currentThread().name} making transaction $i"
                def t = new Transaction(deposit:1.50, code:'A1')
                def result = t.doTransaction(vendor)
                assertNotNull result[0].name
                assertEquals 0.5, result[1]
            }
        }

        Thread.start(runner)
        Thread.start(runner)
        Thread.start(runner)
        Thread.start(runner)
        Thread.start(runner)
        Thread.start(runner)
    }

}