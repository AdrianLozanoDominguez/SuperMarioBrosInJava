package main.java;

import java.awt.event.KeyEvent;

public class MarioController {
    public final boolean RIGHT = true;
    public final boolean LEFT = false;

    private final int maxJumpTime = 34;
    private final int walkSpeed = 5;
    private final int jumpSpeed = 7;
    private final int gravity = PhysicObject.getGravity();
    

    private GameRunner gameRunner;
    private Mario mario;
    
    private boolean[] collisions;
    private int jumpTime;
    private boolean moveRight;
    private boolean moveLeft;
    private boolean jumping;
    private boolean lastDirection;
    

    public MarioController(Mario mario){
        this.mario = mario;
        mario.hCollisionOffset = walkSpeed;
        mario.vCollisionOffset = gravity;
        lastDirection = RIGHT;
        collisions = new boolean[4];
        gameRunner = GameRunner.instance;
    }

    public void keyPressed(int k){
        if (k == KeyEvent.VK_UP) {
            upKeyPressed();
        } else if (k == KeyEvent.VK_RIGHT) {
            rightKeyPressed();
        } else if (k == KeyEvent.VK_LEFT) {
            leftKeyPressed();
        }
    }

    private void upKeyPressed() {
        if(collisions[PhysicObject.COLLISION_BOTTOM]) {
            activateJump();
        }
    }

    private void activateJump(){
        jumpTime = 0;
        jumping = true;
        mario.setLocation(mario.x, mario.y-jumpSpeed);
    }

    private void rightKeyPressed() {
        moveRight = true;
        lastDirection = RIGHT;
    }

    private void leftKeyPressed() {
        moveLeft = true;
        lastDirection = LEFT;
    }

    public void keyReleased(int k) {
        if (k == KeyEvent.VK_UP) {
            upKeyReleased();
        } else if (k == KeyEvent.VK_LEFT) {
            leftKeyReleased();
        } else if (k == KeyEvent.VK_RIGHT) {
            rightKeyReleased();
        }
    }

    private void upKeyReleased() {
        deactivateJump();
    }

    private void deactivateJump() {
        jumping = false;
    }

    private void leftKeyReleased() {
        moveLeft = false;
    }

    private void rightKeyReleased() {
        moveRight = false;
    }

    public void tick(){
        if(jumping)
            jump();

        findCurrentAction();
        applyVelocities();
    }

    public void jump(){
        if(jumpTime > maxJumpTime){
            deactivateJump();
        }
        jumpTime++;
    }

    private void findCurrentAction() {
        if(jumping){
            if(collisions[PhysicObject.COLLISION_TOP]){
                deactivateJump();
                mario.verticalVelocity = gravity;
            }else{
                mario.verticalVelocity = -jumpSpeed;
            }
        }else if(collisions[PhysicObject.COLLISION_BOTTOM]){
            mario.verticalVelocity = 0;
        }else{
            mario.verticalVelocity = gravity;
        }
        
    }

    private void applyVelocities(){
        if (moveRight)
            mario.horizontalVelocity = walkSpeed;
        else if (moveLeft)
            mario.horizontalVelocity = -walkSpeed;
        else
            mario.horizontalVelocity = 0;

        mario.setLocation(mario.x + mario.horizontalVelocity, mario.y + mario.verticalVelocity);
    }

    public void moveCamera(){
        gameRunner.moveHorizontalScroll(mario.x);
    }

    public boolean isMovingRight(){
        return moveRight;
    }

    public boolean isMovingLeft(){
        return moveLeft;
    }

    public boolean isJumping(){
        return jumping;
    }

    public boolean isFalling(){
        return mario.verticalVelocity>0;
    }

    public boolean getLastDirection(){
        return lastDirection;
    }

    public void setCollisions(boolean[] collisions){
        this.collisions = collisions;
    }



    

    
}
