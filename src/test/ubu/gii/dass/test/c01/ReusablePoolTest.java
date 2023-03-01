/**
 * 
 */
package ubu.gii.dass.test.c01;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ubu.gii.dass.c01.DuplicatedInstanceException;
import ubu.gii.dass.c01.NotFreeInstanceException;
import ubu.gii.dass.c01.Reusable;
import ubu.gii.dass.c01.ReusablePool;

/**
 * @author Cordero Bernal, Adrian y Garcia Armario, Jesus
 *
 */
public class ReusablePoolTest {

	private ReusablePool pool;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		pool = ReusablePool.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		pool = null;
	}

	/**
	 * Test method for {@link ubu.gii.dass.c01.ReusablePool#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		// Probamos generar una instancia de ReusablePool
		ReusablePool pool = ReusablePool.getInstance();
		// Comprobamos que no es nula
		assertNotNull(pool);
		// Comprobamos que es una instancia de ReusablePool
		assertTrue(pool instanceof ReusablePool);
	}

	/**
	 * Test method for {@link ubu.gii.dass.c01.ReusablePool#acquireReusable()}.
	 */
	@Test
	public void testAcquireReusable() {
		// Inicializamos
		Reusable object1, object2, object3 = null;

		try {
			// Asignamos el primero
			object1 = pool.acquireReusable();
			// Comprobamos que no sea nulo
			assertNotNull(object1);
			// Comprobamos que es una instancia de Reusable
			assertTrue(object1 instanceof Reusable);
			// Comprobamos que se puede usar
			assertEquals(object1.util(), object1.hashCode()+ "  :Uso del objeto Reutilizable");
			// Asignamos el primero
			object2 = pool.acquireReusable();
			// Comprobamos que no sea nulo
			assertNotNull(object2);
			// Comprobamos que es una instancia de Reusable
			assertTrue(object2 instanceof Reusable);
			// Comprobamos que no sean el mismo objeto
			assertFalse(object1.hashCode() == object2.hashCode());

			// Solicitamos un tercero inexistente para forzar NotFreeInstanceException
			object3 = pool.acquireReusable();

		} catch (NotFreeInstanceException e) {
			assertNull(object3);
		}
	}

	/**
	 * Test method for
	 * {@link ubu.gii.dass.c01.ReusablePool#releaseReusable(ubu.gii.dass.c01.Reusable)}.
	 * 
	 * @throws NotFreeInstanceException
	 */
	@Test
	public void testReleaseReusable() throws NotFreeInstanceException {
		Reusable object1, object2 = null; // Creamos tres objetos vacíos
		int hashId = 0;
		try {
			object1 = pool.acquireReusable(); // Solicitamos el primero
			hashId = object1.hashCode(); // Guardamos su identificador
			pool.releaseReusable(object1); // Liberamos el objeto
			object2 = pool.acquireReusable(); // Solicitamos el segundo
			assertTrue(hashId == object2.hashCode()); // Comprobamos que coinciden 
			pool.releaseReusable(object2); // Liberamos el objet
			pool.releaseReusable(object2); // Forzamos la excepción DuplicatedInstanceException
		} catch (DuplicatedInstanceException e) {
			assertTrue(hashId == object2.hashCode()); // Comprobamos que efectivamente es la misma instancia
		}

	}
}
