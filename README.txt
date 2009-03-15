This is my Groovy implementation of a vending machine for presentation at the May 2009 Lambda Lounge (lambdalounge.org)
in St. Louis, MO.

It is based off the specifications written by Alex Miller (tech.puredanger.com) at
http://stllambdalounge.files.wordpress.com/2009/03/vendingmachinespecification.pdf

To run tests from root, you must have Groovy 1.6.

groovy -cp src/main src/test/VendingMachineTests.groovy
groovy -cp src/main src/test/InventoryLoadingTests.groovy