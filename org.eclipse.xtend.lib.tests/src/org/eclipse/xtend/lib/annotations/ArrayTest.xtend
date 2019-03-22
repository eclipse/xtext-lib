package org.eclipse.xtend.lib.annotations

import org.junit.Test
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull
import com.google.common.collect.Iterables
import java.util.Arrays
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import java.util.List
import org.junit.Ignore

class ArrayTest {
    @Test def void convertAndBackIsSameArray(){
       var String[] array = newArrayOfSize(2)
       var Iterable<String> iter = array;
       var String[] array2 = iter
       assertTrue(array === array2) 
    }
    
    
    @Test def void convertIntegerArray(){
        var int[] array = #[0,1]
        var List<Integer> list = array
        list.set(0,1)
        assertEquals(array.get(0),list.get(0))
        var int[] array2 = list
        assertTrue(array===array2)
         
    }
    
    @Ignore
    @Test def fail(){
        var int[] array = #[0,1]
        var List<Integer> list = array
        var Integer[] array2 = list
        array2.forEach[v, index|
            assertEquals(index,v)
        ]        
    }
    
    @Test def void convertNull(){
       var String[] strings = null
       var Iterable<String> wrapped = strings 
       verifyWrapNull(strings)
       verifyUnvrapNull(wrapped)
    }
    
    private def<T> verifyUnvrapNull(T[] arr){
        assertNull(arr)
    }
    
    private def<T> verifyWrapNull(Iterable<T> iter){
        assertNull(iter)
    }
}