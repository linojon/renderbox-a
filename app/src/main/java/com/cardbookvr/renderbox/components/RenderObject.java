package com.cardbookvr.renderbox.components;

import com.cardbookvr.renderbox.RenderBox;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Jonathan on 12/6/2015.
 */
public class RenderObject extends Component {
    private static final String TAG = "RenderObject";

    protected float[] model = new float[16];

    //Automatically add all render objects to queue
    public RenderObject(){
        super();
        RenderBox.instance.renderObjects.add(this);
    }

    protected static FloatBuffer allocateFloatBuffer(float[] data){
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(data.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bbVertices.asFloatBuffer();
        buffer.put(data);
        buffer.position(0);
        return buffer;
    }

    protected static ShortBuffer allocateShortBuffer(short[] data){
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(data.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        ShortBuffer buffer = bbVertices.asShortBuffer();
        buffer.put(data);
        buffer.position(0);
        return buffer;
    }

    public void draw(float[] view, float[] perspective){
        //Compute position every frame in case it changed
        model = transform.toFloatMatrix();
    }
}
