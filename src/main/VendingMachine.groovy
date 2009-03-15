/*
 * author: Matthew Taylor
 */
class VendingMachine {

    def inventory
    def bank
    def deposit = 0
    
    def pay(amount) {
        deposit += amount
        deposit
    }

    def vend(code) {
        def item = inventory[code]
        if (!item) return "Sorry, no code of '${code}' exists."
        if (item.quantity <= 0) return "Sorry, no more ${item.item.name}, please choose again."
        if (item.price <= deposit) {
            deposit = deposit - item.price
            item.quantity--
            return item.item
        }
    }

    def change() {
        def change = deposit
        deposit = 0
        toCoin(change)
    }

    private def toCoin(change) {
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

    def methodMissing(String name, args) {
        println "methodMissing: $name"
        if (name.startsWith('get')) {
            this.vend(name - 'get')
        } else {
            VendingMachine.metaClass."$name" = { ->
                println "in $name"
                delegate.pay(Coin."$name".value())
            }
            println Coin.quarter
            return this.pay(Coin."$name".value())
        }
    }
}