package cat.dam.alex.mosquitoattack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView iv_mosquit;
    AnimationDrawable mosquit_animat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mosquit_animat = new AnimationDrawable();
        // Carrega el ImageView que contindrà l'animació i actualiza el fons d'imatge amb el recurs XML on es defineix les imatges
        // i temps d'animació del mosquit
        iv_mosquit = (ImageView) findViewById(R.id.iv_mosquit);
        iv_mosquit.setBackgroundResource(R.drawable.mosquit_animat);
        // Obté el fons que ha estat compilat amb un objecte AnimationDrawable
        mosquit_animat = (AnimationDrawable) iv_mosquit.getBackground();
        // Comença l'animació (per defecte repetició de cicle).
        mosquit_animat.start();
    }
    public void start(View button){
        // Configurem l’intent per obrir la segona activitat,
        //com a paràmetres itilitzem aquesta i el nom de la que volem iniciar.
        Intent intent = new Intent(this, SegonaActivity.class);
        // iniciem l’activitat
        startActivity(intent);
    }
}