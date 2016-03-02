/*
 * Copyright (c) 2013 Daniel Strong. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of the author nor the names of other contributors
 *   may not be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package strongdk.examples;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import java.util.logging.Level;
import java.util.logging.Logger;
import strongdk.jme.appstate.console.CommandEvent;
import strongdk.jme.appstate.console.CommandListener;
import strongdk.jme.appstate.console.CommandParser;
import strongdk.jme.appstate.console.ConsoleAppState;
import strongdk.jme.appstate.console.ConsoleDefaultCommandsAppState;

/**
 *This is an example of a jMonkey game with the {@link ConsoleAppState}
 * attached and registers its own commands with the console.
 *
 * @author Daniel Strong <strongd@gmx.com> aka icamefromspace
 * @version 0.99
 * @see http://hub.jmonkeyengine.org/forum/topic/console-appstate-plugin/
 */
public class TestConsoleAppState extends SimpleApplication {

      public static void main(String[] args) {
            Logger.getLogger("").setLevel(Level.WARNING);
            Logger.getLogger("com.jme3").setLevel(Level.WARNING);
            Logger.getLogger("org.lwjgl").setLevel(Level.WARNING);

            AppSettings settings = new AppSettings(true);
            settings.setTitle("Test ConsoleAppState");
            settings.setRenderer(AppSettings.LWJGL_OPENGL_ANY);
            settings.setFrameRate(30);
            settings.setDepthBits(16);
            settings.setResolution(800, 600);

            TestConsoleAppState app = new TestConsoleAppState();
            app.setSettings(settings);
            app.setShowSettings(false);
            app.start();
      }

      private Spatial spatial;
      private float rotationSpeed = 0.5f;
      private ConsoleAppState console;

      @Override
      public void simpleInitApp() {
            console = new ConsoleAppState();
            stateManager.attach(console);
            stateManager.attach(new ConsoleDefaultCommandsAppState());
            console.registerCommand("rotation", commandListener);
            console.appendConsole("You can change speed by using the command 'rotation [1-10]'");
            console.appendConsole("Example: rotation 5");

           BitmapText bt = new BitmapText(assetManager.loadFont("Interface/Fonts/Default.fnt"));
           bt.setColor(ColorRGBA.Black);
           bt.setText("This is an example of a game that uses ConsleAppState\nUse the grave key (button next to 1) to toggle the console on and off");
           bt.setLocalTranslation(0, bt.getLineHeight() *4,0);
           bt.setBox(new Rectangle(0, 0, guiViewPort.getCamera().getWidth(), bt.getLineHeight()));
           bt.setAlignment(BitmapFont.Align.Center);
           guiNode.attachChild(bt);


            setupSceneBoxGeom();
      }

      @Override
      public void simpleUpdate(float tpf) {
            super.simpleUpdate(tpf);
            spatial.rotate(0, rotationSpeed * tpf, 0);
      }

      public void setRotationSpeed(float newSpeed) {
            rotationSpeed = newSpeed;
      }

      private CommandListener commandListener = new CommandListener() {
            @Override
            public void execute(CommandEvent evt) {
                  final CommandParser parser = evt.getParser();
                  if (evt.getCommand().equals("rotation")) {
                        Integer value = parser.getInt(0);
                        if(value != null){
                              setRotationSpeed(value / 10f);
                              console.appendConsole("Rotation speed changed: "+value);
                        }else{
                              console.appendConsoleError("Could not change speed, not a valid number: " + parser.getString(0));
                        }
                  }
            }

      };

      private void setupSceneBoxGeom() {
            getFlyByCamera().setEnabled(false);
            getViewPort().setBackgroundColor(new ColorRGBA(100 / 255f, 149 / 255f, 237 / 255f, 1f));
            spatial = new Geometry("Box", new Box(Vector3f.ZERO, 1, 1, 1));
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Blue);
            spatial.setMaterial(mat);
            spatial.rotate(20 * FastMath.DEG_TO_RAD, 0, 0);
            spatial.move(0.1f, -0.2f, 0);
            rootNode.attachChild(spatial);
      }

}
