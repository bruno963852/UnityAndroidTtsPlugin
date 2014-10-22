package instituto.iracema.unityttsplugin;
import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class TextSpeech  
{
	//Gerenciador de tts
	private TextToSpeech myTTS;
	//Avisa quanto o tts já pode falar
	private boolean readyToSpeak = false;
	//Context da aplicação
	private Context context;
	
	//Construtor
	public TextSpeech(Context baseContext)
	{
		//Pega o context
		this.context = baseContext;
		
		//Inicializa o gerenciador de tts
		myTTS = new TextToSpeech(context, new OnInitListener() 
		{
			//Evento gerado ao inicializar
			@Override
			public void onInit(int status) 
			{
				//Se inicializou com sucesso
				if (status == TextToSpeech.SUCCESS) 
				{
					//Tenta setar a língua para o padrão do sistema
					int result = myTTS.setLanguage( Locale.getDefault());
					//Se não conseguiu
					if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
					{
						//tenta Setar para inglês
						Log.e("TTS", "Portuguese is not supported");
						result = myTTS.setLanguage(Locale.ENGLISH);
					}
					//Se não conseguiu
					if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) 
					{
						//Senta e chora
						Log.e("TTS", "This Language is not supported");
					}
					//Avisa que já está pronto pra falar
					readyToSpeak = true;
				}
				//Se não inicializou com sucesso
				else
				{
					//Tenta instalar o TTS
					installTTS();
					readyToSpeak = true;
				}
			}
			
		});
	}
	
	//Método para instalar TTS
	private void installTTS()
	{
		//Inicializa o intent
		Intent installIntent = new Intent();
		//Seta a flag
		installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//Seta a action
		installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
		//Inicia a activity
		context.startActivity(installIntent);
	}
	
	//Fala uma string
	public void speak(String text)
	{
		//Se está pronto para falar
		if (readyToSpeak)
		{
			//Fala uma string interrompendo o que já estava sendo falado
			myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			Log.i("TTS", "Falando: " + text);
		}
	}
	
	//Fala uma string, após o que já está sendo falado
	public void speakAdding(String text)
	{
		//Se está pronto para falar
		if (readyToSpeak)
		{
			//Adiciona a fala na fila do TTS, não interrompendo a fala atual
			myTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
			Log.i("TTS", "Falando: " + text);
		}
	}
	
	//Diz se está falando
	public boolean isSpeaking()
	{
		return myTTS.isSpeaking();
	}
	
	//Para de falar
	public void stopSpeaking()
	{
		myTTS.stop();
	}
	
	//Adiciona um som para a engine de tts associado a uma string
	public void addEarcon(String earcon, String filename)
	{
		myTTS.addEarcon(earcon, filename);
	}
	
	//Adiciona uma fala para a engine de tts associada a uma string
	public void addSpeech(String speech, String filename)
	{
		myTTS.addSpeech(speech, filename);
	}
	
	//Toca um som previamente adicionado
	public void playEarcon(String earcon)
	{
		//Se está pronto para falar
		if (readyToSpeak)
		{
			//Toca o som, interrompendo o que está sendo falado ou tocado antes
			myTTS.playEarcon(earcon, TextToSpeech.QUEUE_FLUSH, null);
			Log.i("TTS", "Tocou: " + earcon);
		}
	}
	
	//Toca um som previamente adicionado, sem interromper a fala atual
	public void playEarconAdding(String earcon)
	{
		//Se está pronto para falar
		if (readyToSpeak)
		{
			//Toca o som, sem interrompoer o que está sendo falado ou tocado antes
			myTTS.playEarcon(earcon, TextToSpeech.QUEUE_ADD, null);
			Log.i("TTS", "Tocou: " + earcon);
		}
	}
	
	//Sintetiza uma fala em arquivo de som
	public void synthesizeToFile(String text, String filename)
	{
		myTTS.synthesizeToFile(text, null, filename);
	}
	
	//Seta o pitchs
	public void setPitch(float pitch)
	{
		myTTS.setPitch(pitch);
	}
	
	//Seta a velocidade da fala
	public void setSpeechRate(float speechRate)
	{
		myTTS.setSpeechRate(speechRate);
	}
}
