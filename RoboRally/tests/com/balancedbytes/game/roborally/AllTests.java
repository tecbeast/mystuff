package com.balancedbytes.game.roborally;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.balancedbytes.game.roborally.model.factory.FactoryBoardTest;
import com.balancedbytes.game.roborally.model.factory.FactoryElementsTest;

@RunWith(Suite.class)
@SuiteClasses({
	FactoryElementsTest.class,
	FactoryBoardTest.class
})
public class AllTests {

}
