package com.cardbookvr.renderbox;

import com.cardbookvr.renderbox.math.Matrix4;
import com.cardbookvr.renderbox.math.Vector3;

/**
 * Created by Jonathan on 12/6/2015.
 */
public class Transform {
    private static final String TAG = "RenderBox.Transform";

    private Vector3 localPosition = new Vector3(0,0,0);
    private Vector3 localRotation = new Vector3(0,0,0);
    private Vector3 localScale = new Vector3(1,1,1);

    private Transform parent = null;

    /*==================
    PARENT METHODS
    ====================*/
    public void setParent(Transform Parent){
        setParent(parent, true);
    }
    public void setParent(Transform parent, boolean updatePosition){
        if(this.parent == parent)       //Early-out if setting same parent--don't do anything
            return;
        if(parent == null){
            unParent(updatePosition);
            return;
        }
        if(updatePosition){
            Vector3 tmp_position = getPosition();
            //TODO: rotaion and scale
            this.parent = parent;
            setPosition(tmp_position);
            //TODO: rotation and scale
        } else {
            this.parent = parent;
        }
        //TODO: add to child list
    }
    public void upParent(){
        unParent(true);
    }
    public void unParent(boolean updatePosition){
        if(parent == null)      //Early out--we already have no parent
            return;
        if(updatePosition){
            localPosition = getPosition();
            //TODO: set rotation and scale
        }
        //TODO: remove from children array
        parent = null;
    }

    /*==================
    POSITION METHODS
    ====================*/
    public void setPosition(float x, float y, float z){
        if(parent != null){
            this.localPosition = new Vector3(x,y,z).subtract(parent.getPosition());
        } else {
            this.localPosition = new Vector3(x, y, z);
        }
    }
    public void setPosition(Vector3 position){
        if(parent != null){
            this.localPosition = new Vector3(position).subtract(parent.getPosition());
        } else {
            this.localPosition = position;
        }
    }
    public Vector3 getPosition(){
        if(parent != null){
            return Matrix4.TRS(parent.getPosition(), parent.getRotation(), parent.getScale()).multiplyPoint3x4(localPosition);
        }
        //TODO: return a copy?
        return localPosition;
    }
    public void setLocalPosition(float x, float y, float z){
        this.localPosition = new Vector3(x, y, z);
    }
    public void setLocalPosition(Vector3 position){
        this.localPosition = position;
    }
    public Vector3 getLocalPosition(){
        //TODO: return a copy?
        return localPosition;
    }

    /*==================
    ROTATION METHODS
    ====================*/
    public void setRotation(float yaw, float pitch, float roll){
        if(parent != null){
            this.localRotation = parent.getRotation().add(new Vector3(pitch, yaw, roll));
        } else {
            this.localRotation = new Vector3(pitch, yaw, roll);
        }
    }

    public void setRotation(Vector3 rotation){
        if(parent != null){
            this.localRotation = parent.getRotation().add(rotation);
        } else {
            this.localRotation = rotation;
        }
    }

    public Vector3 getRotation(){
        if(parent != null){
            return new Vector3(localRotation).add(parent.getRotation());
        }
        return localRotation;
    }

    public void setLocalRotation(float yaw, float pitch, float roll){
        this.localRotation = new Vector3(pitch, yaw, roll);
    }

    public void setLocalRotation(Vector3 eulerAngles){
        this.localRotation = eulerAngles;
    }

    public Vector3 getLocalRotation(){
        return localRotation;
    }

    public void rotate(float yaw, float pitch, float roll){
        localRotation.add(pitch, yaw, roll);
    }

    public Vector3 getScale(){
        if(parent != null){
            return new Vector3(parent.getScale()).scale(localScale);
        }
        //TODO: return a copy?
        return localScale;
    }

    /*==================
    SCALE METHODS
    ====================*/
    public void setLocalScale(float x, float y, float z){
        this.localScale = new Vector3(x,y,z);
    }
    public void setLocalScale(Vector3 scale){
        this.localScale = scale;
    }
    public Vector3 getLocalScale(){
        //TODO: return a copy?
        return localScale;
    }
    public void scale(float x, float y, float z){
        this.localScale.scale(x, y, z);
    }
    public float[] toFloatMatrix(){
        //Log.d(TAG, getPosition() + ", " + getRotation() + ", " + getScale());
        return Matrix4.TRS(getPosition(), getRotation(), getScale()).val;
    }
}
