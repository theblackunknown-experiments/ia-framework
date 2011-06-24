package org.eisti.labs.game;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author MACHIZAUD Andréa
 * @version 6/20/11
 */
@Deprecated
public class AbstractPawnTest {

    @Mock
    private AbstractPawn pawnMock = mock(AbstractPawn.class);

    @Test
    public void correctIdentifier() {
        String pawnName = "Dummy Pawn";

        when(pawnMock.getName())
                .thenReturn(pawnName);
        when(pawnMock.getIdentifier())
                .thenReturn(pawnName.hashCode());



        assertEquals("Name did not match",
                pawnName,
                pawnMock.getName());
        assertEquals("Identifier did not match",
                pawnName.hashCode(),
                pawnMock.getIdentifier());
    }

}
