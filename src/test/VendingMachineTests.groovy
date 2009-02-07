/*
 * author: Matthew Taylor
 */
class VendingMachineTests extends GroovyTestCase {

    def vendor

    void setUp() {
        vendor = new VendingMachine()
        def inventory = [
                D8: [item:[name:'Baby Ruth'], price: 1.0, quantity: 1000],
                D9: [item:[name:'Mr. Goodbar'], price: 1.0, quantity: 1000]
        ]
        vendor.inventory = inventory
    }

    void testVend() {
        assertEquals 1, vendor.pay(1)
        assertEquals 'Mr. Goodbar', vendor.vend('D9').name
        assertEquals 0, vendor.deposit

        assertEquals 1, vendor.pay(1)
        assertEquals 'Baby Ruth', vendor.vend('D8').name
        assertEquals 0, vendor.deposit
    }

    void testNotEnoughMoneyWontVend() {
        assertEquals 0.5, vendor.pay(0.5)
        assertEquals null, vendor.vend('D8')
        assertEquals 0.5, vendor.deposit
    }

    void testTooMuchMoneyReturnsChangeAfterVend() {
        assertEquals 0.25, vendor.pay(0.25)
        assertEquals 1.25, vendor.pay(1)
        vendor.vend('D8')
        assertEquals 0.25, vendor.deposit
        assertEquals 0.25, vendor.change()
        assertEquals 0, vendor.deposit
    }

    void testCallingChangeBeforeVendReturnsCorrectAmount_AndClearsDeposit() {
        assertEquals 1, vendor.pay(1)
        assertEquals 1, vendor.change()
        assertEquals null, vendor.vend('D9')
        assertEquals 0, vendor.deposit
    }

    void testVendingWhenNoCodeExists() {
        assertEquals 'Sorry, no code of \'NN\' exists.', vendor.vend('NN')
    }

    void testVendingWhenNoInventoryReturnsErrorMessage() {
        vendor.inventory.A1 = [item:[name:'Zagnut'], price:1, quantity:0]
        vendor.pay(1)
        assertEquals 'Sorry, no more Zagnut, please choose again.', vendor.vend('A1')
        assertEquals 1, vendor.deposit
    }

    void testVendingDecreasesQuantity() {
        def count = vendor.inventory.D9.quantity
        vendor.pay(1)
        vendor.vend('D9')
        assertEquals count - 1, vendor.inventory.D9.quantity
    }

    void testVendingDoesNotDecreaseQuantityWhenNoVendOccurs() {
        vendor.inventory.A1 = [item:[name:'Zagnut'], price:1, quantity:10]
        vendor.pay(0.5)
        vendor.vend('A1')   // no vend should occur here because not enough $$
        assertEquals 10, vendor.inventory.A1.quantity
    }

    void testVendingTransaction() {
        def t = new Transaction(deposit:1.50, code:'D9')
        def result = t.doTransaction(vendor)
        assertEquals 'Mr. Goodbar', result[0].name
        assertEquals 0.5, result[1]
    }

}