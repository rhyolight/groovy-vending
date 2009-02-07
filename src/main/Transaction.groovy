/*
 * author: Matthew Taylor
 */
class Transaction {

    def deposit
    def code

    def doTransaction(vendor) {
        vendor.pay(deposit)
        def vendedItem = vendor.vend(code)
        def change = vendor.change()
        [vendedItem, change]
    }

}