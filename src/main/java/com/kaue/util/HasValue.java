package com.kaue.util;

import java.util.Date;

/**
 * @author wesley
 *
 */
public class HasValue
{
	/**
	 * Verifica se a string e nao nula e nao vazia.
	 * @param str
	 * <ul><li>Objeto do tipo String</li></ul>
	 * @return
	 * <ul>
	 *   <li><code>true</code> se a condicao for verdadeira</li>
	 *   <li><code>false</code> se a condicao for falsa</li>
	 * </ul>
	 */
	public static boolean execute(String val)
	{
		if (val != null && !val.trim().equals("") && val.trim().length() > 0)
			return true;

		return false;
	}
	
	/**
	 * Verifica se o Integer e nao nula e maior que 0 (zero).
	 * @param val
	 * <ul><li>Objeto do tipo Integer</li></ul>
	 * @return
	 * <ul>
	 *   <li><code>true</code> se a condicao for verdadeira</li>
	 *   <li><code>false</code> se a condicao for falsa</li>
	 * </ul>
	 */
	public static boolean execute(Integer val)
	{
		if (val != null && val > 0)
			return true;

		return false;
	}
	
	/**
	 * Verifica se o Double e nao nula e maior que 0 (zero).
	 * @param val
	 * <ul><li>Objeto do tipo Double</li></ul>
	 * @return
	 * <ul>
	 *   <li><code>true</code> se a condicao for verdadeira</li>
	 *   <li><code>false</code> se a condicao for falsa</li>
	 * </ul>
	 */
	public static boolean execute(Double val)
	{
		if (val != null && val > 0)
			return true;

		return false;
	}
	
	/**
	 * Verifica se o Date e nao nula.
	 * @param val
	 * <ul><li>Objeto do tipo Date</li></ul>
	 * @return
	 * <ul>
	 *   <li><code>true</code> se a condicao for verdadeira</li>
	 *   <li><code>false</code> se a condicao for falsa</li>
	 * </ul>
	 */
	public static boolean execute(Date val)
	{
		if (val != null)
			return true;

		return false;
	}
	
	/**
	 * Verifica se o Objeto e nao nulo.
	 * @param val
	 * <ul><li>Objeto do tipo Date</li></ul>
	 * @return
	 * <ul>
	 *   <li><code>true</code> se a condicao for verdadeira</li>
	 *   <li><code>false</code> se a condicao for falsa</li>
	 * </ul>
	 */
	public static boolean execute(Object val)
	{
		if (val != null)
			return true;

		return false;
	}
}