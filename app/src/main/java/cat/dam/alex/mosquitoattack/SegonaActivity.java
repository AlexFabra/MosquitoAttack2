package cat.dam.alex.mosquitoattack;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class SegonaActivity extends AppCompatActivity {

    private TextView tv_scorepoints;
    private int points=0;
    private TextView tv_timer;
    private int timer=30;
    private final int MILIS_TIMER=30000;
    private final int SECONDS_UNTIL_MOSQUITO_GENERATION = 3000;
    //creem l'arrayList que guardarà les imatges dels mosquits:
    ArrayList<ImageView> iv_mosquitoes= new ArrayList<>();
    //Creem l'arrayList que guardarà les animacions dels mosquits:
    ArrayList<AnimationDrawable> anim_mosquitoes= new ArrayList<>(); 
    //Creem una instància d'un Random per escollir la posició cardinal y dels mosquits:
    Random rand = new Random();
    //Les instàncies de Handler i Runnable ens serviràn per fer que l'estat del joc canvii en el temps:
    Handler handler = new Handler();
    Handler handler2 = new Handler();
    Runnable runnable;
    Runnable runnable2;
    //MosquitoFlyingVelocity servirà per determinar la velocitat de canvi de posició horitzontal dels mosquits:
    int mosquitoFlyingVelocity= 50;
    private RelativeLayout mosquitosPlace;
    int mosquitoNumber=0;
    private int HEIGHT_MOSQUITOS_PLACE;
    private int WIDTH_MOSQUITOS_PLACE;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segona);

        tv_scorepoints = (TextView) findViewById(R.id.tv_scorepoints);
        tv_scorepoints.setText(Integer.toString(points));
        tv_timer = (TextView) findViewById(R.id.tv_time);
        mosquitosPlace= (RelativeLayout) findViewById(R.id.ll_mosquitosPlace);

        //Aquest runnable s'executa quan els valors del activity s'han llegit,
        //així podrem llegir les mides del layout.
        mosquitosPlace.post(new Runnable() {
           @Override
           public void run() {
               HEIGHT_MOSQUITOS_PLACE = mosquitosPlace.getHeight()-250;
               WIDTH_MOSQUITOS_PLACE = mosquitosPlace.getWidth()-250;
               generaMosquits();
               iniciaComptador();
           }
        });
    }

    private void iniMosquito(int mosquitoNumber) {
        int mn=mosquitoNumber;
        //creem arrays per afegir els elements i poder utilitzar-los fora d'aquesta funció:
        final int[] HORIZONTAL_POSITION = {1};
        final boolean[] mosquitoAlive = {true};
        AnimationDrawable anim_mosquit= new AnimationDrawable();
        final AnimationDrawable[] finalAnim_mosquit = {anim_mosquit};
        //afegim un mosquit a l'arraylist de mosquits:
        iv_mosquitoes.add(new ImageView(this));
        iv_mosquitoes.get(mn).setX(HORIZONTAL_POSITION[0]);
        iv_mosquitoes.get(mn).setY(rand.nextInt(HEIGHT_MOSQUITOS_PLACE));
        mosquitosPlace.addView(iv_mosquitoes.get(mn)); 
        iv_mosquitoes.get(mn).setBackgroundResource(R.drawable.mosquit_animat);
        anim_mosquit = (AnimationDrawable) iv_mosquitoes.get(mn).getBackground();
        anim_mosquitoes.add(anim_mosquit);
        //la animació s'activa:
        anim_mosquit.start();

        //creem aquesta copia del AnimationDrawable per accedir a ell a dins del runnable:
        AnimationDrawable finalAnim_mosquit1 = anim_mosquit;
        handler.postDelayed(runnable = new Runnable(){
            public void run(){
                //si la variable mosquitoAlive està en true el ImageView es mou 10 a la dreta.
                if(mosquitoAlive[0]) {
                    HORIZONTAL_POSITION[0] += 10;
                    iv_mosquitoes.get(mn).setX(HORIZONTAL_POSITION[0]);
                    handler.postDelayed(this, mosquitoFlyingVelocity);
                }
                //si el mosquit arriba al final del layout...
                if(WIDTH_MOSQUITOS_PLACE+300<iv_mosquitoes.get(mn).getX()){
                    handler.removeCallbacks(this); //el Handler deixa d'executar el Runnable.
                    finalAnim_mosquit1.stop(); //l'animació es para.
                }
            }
        }, mosquitoFlyingVelocity);
        //quan es cliqui el mosquit, es dispararà la funció mosquitosMurder
        iv_mosquitoes.get(mn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mosquitosMurder(view,finalAnim_mosquit,mosquitoAlive);
            }
        });
    }

    public void incrementScore(){
        points+=1;
        setScore(points);
    }
    public void setScore(int points){
        tv_scorepoints.setText(Integer.toString(points));
    }

    /** funció per reiniciar el Activity
     * @param restart
     */
    public void restart(View restart){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /** generaMosquits genera un mosquit en un interval definit per
     *  SECONDS_UNTIL_MOSQUITO_GENERATION fins que arriba al valor int de MILIS_TIMER
     *  Quan termina, inicialitza el final del joc.
     */
    public void generaMosquits(){
        new CountDownTimer(MILIS_TIMER,SECONDS_UNTIL_MOSQUITO_GENERATION){
            public void onTick(long millisUntilFinished){
                iniMosquito(mosquitoNumber);
                mosquitoNumber++;
            }
            public void onFinish(){
            }
        }.start();
    }

    /** iniciaComptador inicia un comptador que compta
     *  segón per segón i actualitza la variable int timer
     *  i el View TextView tv_timer
     *  fins arribar al valor int de MILIS_TIMER
     */
    public void iniciaComptador(){
        new CountDownTimer(MILIS_TIMER,1000){
            public void onTick(long millisUntilFinished){
                tv_timer.setText(String.valueOf(timer));
                timer--;
            }
            public void onFinish(){
                String mensaje = points+" points earned";
                Toast.makeText(SegonaActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /** mosquitosMurder cambia la imagen del mosquito por la de la sangre
     * @param view ImageView mosquito
     * @param finalAnim_mosquit AnimationDrawable del mosquito
     * @param mosquitoAlive boolean 
     */
    public void mosquitosMurder(View view,final AnimationDrawable[] finalAnim_mosquit,final boolean[] mosquitoAlive){
        incrementScore();
        //canviem l'animació del mosquit per la sang:
        view.setBackgroundResource(R.drawable.sang_animat);
        finalAnim_mosquit[0] = (AnimationDrawable) view.getBackground();
        anim_mosquitoes.add(finalAnim_mosquit[0]);
        finalAnim_mosquit[0].start();
        mosquitoAlive[0] =false;
        //si reduim el valor de mosquitoFlyingVelocity els mosquits aniràn més ràpid:
        mosquitoFlyingVelocity-=5;
        //per que no es pugui fer click a la sang.
        view.setEnabled(false);
        handler2.postDelayed(runnable2 = new Runnable(){
            public void run(){
                if(finalAnim_mosquit[0].getAlpha()>10) { //si el alpha de la animació no és menor a 10...
                    finalAnim_mosquit[0].setAlpha(finalAnim_mosquit[0].getAlpha() - 10); //es redueix 10
                } else {
                    finalAnim_mosquit[0].setAlpha(0); //la animació desapareix completament.
                    handler2.removeCallbacks(this); //el Handler deixa d'executar el Runnable.
                }
                handler2.postDelayed(this,250); //s'executa cada quart de segón.
            }
        }, 1000); //s'executa després d'un segón.
    }
}
