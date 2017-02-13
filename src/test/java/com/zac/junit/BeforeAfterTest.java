package com.zac.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BeforeAfterTest {

	@BeforeClass
	public static void execBeforeClass() {
		System.out.println("*** BeforeClass.");
	}
	@Before
	public void execBefore() {
		System.out.println("*.Before.");
	}

	@Test
	public void hogeHogeTest() {
		System.out.println("  hogehoge");
	}
	@Test
	public void fugaFugaTest() {
		System.out.println("  fugafuga");
	}

	@Test
	public void piyoPiyoTest() {
		System.out.println("  piyopiyo");
	}

	@After
	public void execAfter() {
		System.out.println("@.After.");
	}

	@AfterClass
	public static void execAfterClass() {
		System.out.println("*** AfterClass.");
	}
}
