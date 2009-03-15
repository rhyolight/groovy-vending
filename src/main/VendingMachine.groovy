/*
 * author: Matthew Taylor
 */
class VendingMachine {

    def inventory
    def bank
    def deposit = 0
    def serviceMode = false

    void setInventory(inventory) {
        if (!serviceMode) {
            throw new VendingException('Cannot execute command unless in service mode')
        }
        this.@inventory = inventory
    }

    void setBank(bank) {
        if (!serviceMode)
            throw new VendingException('Cannot execute command unless in service mode')
        this.@bank = bank
    }
    
    def pay(amount) {
        deposit += amount
        deposit
    }

    def vend = { code ->
        def item = inventory[code]
        if (!item) return "Sorry, no code of '${code}' exists."
        if (item.quantity <= 0) return "Sorry, no more ${item.name}, please choose again."
        if (item.price <= deposit) {
            deposit = deposit - item.price
            item.quantity--
            return [item, coinReturn()]
        }
    }

    def coinReturn() {
        def change = deposit
        deposit = 0
        toCoin(change)
    }

    private toCoin(change) {
        def coins = []
        Coin.values().each { coin ->
            // while change to give is greater than coin value, and there are more coins in the bank to give
            while (change >= coin.value() && bank[coin]) {
                coins << coin
                bank[coin]--
                change -= coin.value()
            }
        }
        coins
    }

    def propertyMissing(String name) {
        this.vend(name)
    }

    def methodMissing(String name, args) {
        if (name in Coin.values().collect {it.name()}) {
            def newMethod = { ->
                delegate.bank[Coin."$name"]++
                delegate.pay(Coin."$name".value())
            }
            VendingMachine.metaClass."$name" = newMethod
            newMethod.call(args)
        }
    }
}