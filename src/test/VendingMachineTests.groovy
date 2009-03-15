/*
 * author: Matthew Taylor
 */
class VendingMachineTests extends GroovyTestCase {

    def vendor

    void setUp() {
        vendor = new VendingMachine()
        def inventory = [
                A:[item:[name:'item a'], price: 0.65, quantity: 1000],
                B:[item:[name:'item b'], price: 1, quantity: 1000],
                C:[item:[name:'item c'], price: 1.5, quantity: 1000],
                D8: [item:[name:'Baby Ruth'], price: 1.0, quantity: 1000],
                D9: [item:[name:'Mr. Goodbar'], price: 1.0, quantity: 1000]
        ]
        vendor.inventory = inventory
        vendor.bank = [
                (Coin.dollar):100,
                (Coin.quarter):100,
                (Coin.dime):100,
                (Coin.nickel):100
        ]
    }

    void testVend() {
        assertEquals 1, vendor.dollar()
        assertEquals 'Mr. Goodbar', vendor.vend('D9').name
        assertEquals 0, vendor.deposit

        assertEquals 1, vendor.dollar()
        assertEquals 'Baby Ruth', vendor.vend('D8').name
        assertEquals 0, vendor.deposit

        assertEquals 102, vendor.bank[Coin.dollar]
        assertEquals 100, vendor.bank[Coin.quarter]
        assertEquals 100, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testNotEnoughMoneyWontVend() {
        assertEquals 0.25, vendor.quarter()
        assertEquals 0.5, vendor.quarter()
        assertEquals null, vendor.vend('D8')
        assertEquals 0.5, vendor.deposit

        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 102, vendor.bank[Coin.quarter]
        assertEquals 100, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testTooMuchMoneyReturnsChangeAfterVend() {
        assertEquals 0.25, vendor.quarter()
        assertEquals 1.25, vendor.dollar()
        vendor.vend('D8')
        assertEquals 0.25, vendor.deposit
        def change = vendor.change()
        assertEquals 1, change.size()
        assertEquals Coin.quarter, change[0]
        assertEquals 0, vendor.deposit

        assertEquals 101, vendor.bank[Coin.dollar]
        assertEquals 100, vendor.bank[Coin.quarter]
        assertEquals 100, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testCallingChangeBeforeVendReturnsCorrectAmount_AndClearsDeposit() {
        assertEquals 1, vendor.dollar()
        assertChangeEquals 1, vendor.change()
        assertEquals null, vendor.vend('D9')
        assertEquals 0, vendor.deposit

        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 100, vendor.bank[Coin.quarter]
        assertEquals 100, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testVendingWhenNoCodeExists() {
        assertEquals 'Sorry, no code of \'NN\' exists.', vendor.vend('NN')
    }

    void testVendingWhenNoInventoryReturnsErrorMessage() {
        vendor.inventory.A1 = [item:[name:'Zagnut'], price:1, quantity:0]
        vendor.dollar()
        assertEquals 'Sorry, no more Zagnut, please choose again.', vendor.vend('A1')
        assertEquals 1, vendor.deposit

        assertEquals 101, vendor.bank[Coin.dollar]
        assertEquals 100, vendor.bank[Coin.quarter]
        assertEquals 100, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testVendingDecreasesQuantity() {
        def count = vendor.inventory.D9.quantity
        vendor.dollar()
        vendor.vend('D9')
        assertEquals count - 1, vendor.inventory.D9.quantity

        assertEquals 101, vendor.bank[Coin.dollar]
        assertEquals 100, vendor.bank[Coin.quarter]
        assertEquals 100, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testVendingDoesNotDecreaseQuantityWhenNoVendOccurs() {
        vendor.inventory.A1 = [item:[name:'Zagnut'], price:1, quantity:10]
        vendor.quarter()
        vendor.quarter()
        vendor.vend('A1')   // no vend should occur here because not enough $$
        assertEquals 10, vendor.inventory.A1.quantity

        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 102, vendor.bank[Coin.quarter]
        assertEquals 100, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testBuyWithQuarters() {
        vendor.quarter()
        vendor.quarter()
        vendor.quarter()
        vendor.quarter()
        assertEquals 'item a', vendor.getA().name
    }

    void testQuarterAndChange() {
        vendor.quarter()
        vendor.quarter()
        assertChangeEquals 0.5, vendor.change()
        assertEquals 0, vendor.deposit
    }

    void testToCoin_25() {
        def coins = vendor.toCoin(0.25)
        assertEquals 1, coins.size()
        assertEquals Coin.quarter, coins[0]
        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 99, vendor.bank[Coin.quarter]
        assertEquals 100, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testToCoin_30() {
        def coins = vendor.toCoin(0.30)
        assertEquals 2, coins.size()
        assertEquals Coin.quarter, coins[0]
        assertEquals Coin.nickel, coins[1]
        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 99, vendor.bank[Coin.quarter]
        assertEquals 100, vendor.bank[Coin.dime]
        assertEquals 99, vendor.bank[Coin.nickel]
    }

    void testToCoin_35() {
        def coins = vendor.toCoin(0.35)
        assertEquals 2, coins.size()
        assertEquals Coin.quarter, coins[0]
        assertEquals Coin.dime, coins[1]
        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 99, vendor.bank[Coin.quarter]
        assertEquals 99, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testToCoin_40() {
        def coins = vendor.toCoin(0.4)
        assertEquals 3, coins.size()
        assertEquals Coin.quarter, coins[0]
        assertEquals Coin.dime, coins[1]
        assertEquals Coin.nickel, coins[2]
        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 99, vendor.bank[Coin.quarter]
        assertEquals 99, vendor.bank[Coin.dime]
        assertEquals 99, vendor.bank[Coin.nickel]
    }

    void testToCoin_70() {
        def coins = vendor.toCoin(0.7)
        assertEquals 4, coins.size()
        assertEquals Coin.quarter, coins[0]
        assertEquals Coin.quarter, coins[1]
        assertEquals Coin.dime, coins[2]
        assertEquals Coin.dime, coins[3]
        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 98, vendor.bank[Coin.quarter]
        assertEquals 98, vendor.bank[Coin.dime]
        assertEquals 100, vendor.bank[Coin.nickel]
    }

    void testToCoin_WhenMissingQuarters() {
        vendor.bank[Coin.quarter] = 0
        def coins = vendor.toCoin(0.75)
        assertEquals 8, coins.size()
        assertEquals Coin.dime, coins[0]
        assertEquals Coin.dime, coins[1]
        assertEquals Coin.dime, coins[2]
        assertEquals Coin.dime, coins[3]
        assertEquals Coin.dime, coins[4]
        assertEquals Coin.dime, coins[5]
        assertEquals Coin.dime, coins[6]
        assertEquals Coin.nickel, coins[7]
        assertEquals 100, vendor.bank[Coin.dollar]
        assertEquals 0, vendor.bank[Coin.quarter]
        assertEquals 93, vendor.bank[Coin.dime]
        assertEquals 99, vendor.bank[Coin.nickel]
    }

    private assertChangeEquals(double amount, change) {
        assertEquals amount, change.collect { it.value() }.sum() 
    }

}