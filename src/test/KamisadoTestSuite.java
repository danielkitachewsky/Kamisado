package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.kamisado.BoardTest;
import test.kamisado.PositionTest;

@RunWith(Suite.class)
@SuiteClasses({ BoardTest.class, PositionTest.class, })
public class KamisadoTestSuite {

}
