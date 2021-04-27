package mario.ui;

import nl.han.ica.oopg.userinput.IMouseInput;

/**
 * An abstract class for receiving mouseEvents.
 */
public abstract class MouseAdapter implements IMouseInput {


    @Override
    public void mousePressed(int i, int i1, int i2) {}

    @Override
    public void mouseReleased(int i, int i1, int i2) {}

    @Override
    public void mouseClicked(int i, int i1, int i2) {}

    @Override
    public void mouseMoved(int i, int i1) {}

    @Override
    public void mouseDragged(int i, int i1, int i2) {}

    @Override
    public void mouseWheel(int i) {}
}