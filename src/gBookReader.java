import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import Java.util.Stack;
import java.io.InputStream;
import java.io.EOFException;
import java.io.IOException;
public class gBookReader extends MIDlet {
	private Display display; // менеджер дисплея
	private BookCanvas fCanvas; // экранный фрагмент книги

	public void destroyApp(boolean flag) {
	}
	public void pauseApp() {}
	// стартовый метод мидлета
	public void startApp() {
		// получить ссылку на менеджер дисплея
		display = Display.getDisplay(this);
		// создать новую форму с именем основного класса
		Form form = new Form(getClass().getName());
		// создать объект экранного фрагмента книги
		fCanvas =new BookCanvas();
		// отобразить экранный фрагмент книги
		display.setCurrent(fCanvas);
	}

	// класс экранного фрагмента книги
	public class BookCanvas extends Canvas {
		private Stack PageIndex; // стек смещений страниц от начала текста
		public BookCanvas() {
			// создать новый объект стека номеров страниц
			PageIndex = new Stack();
		}
		// метод перерисовки объекта экранного фрагмента
		protected void paint(Graphics g) {
			// получить ширину и высоту рабочей области экрана
			int gw = g.getClipWidth();
			int gh = g.getClipHeight();
			// создать шрифт
			Font font = Font.getFont(Font.FACE_MONOSPACE,
									Font.STYLE_PLAIN,
									Font.SIZE_SMALL);
			// установить шрифт
			g.setFont(font);
			// инициализировать поток
			InputStream is = getClass().getResourceAsStream("/story.txt");
			MyDataInputStream mdis = new MyDataInputStream(is);
			int x=0,y=0; // текущее положение вывода на экране
			int offset=0; // смещение по исходному тексту
			// очистка экрана
			g.setColor(255,255,255);
			g.fillRect(0,0,gw,gh);
			g.setColor(0,0,0);
			// получить смещение для очередной страницы
			if(!PageIndex.empty())
				offset=((Integer)PageIndex.peek()).intValue();
				try{
					// сместиться по тексту до необходимой страницы
					mdis.skipBytes(offset);
				} catch(IOException ioe) {}
				String sWord;
				do {
					// прочитать очередное слово из потока
					sWord = mdis.readWord();
					// продвинуть смещение по файлу на одно слово
					offset+=sWord.length();
					// если текущая позиция и пиксельная ширина слова не выходят за экран
					if(x+font.stringWidth(sWord)<=gw) {
						// отобразить слово, начиная с текущей позиции
						g.drawString(sWord, x,y, g.TOP|g.LEFT);
						// сместить текущую позицию на пиксельную ширину слова
						x+=font.stringWidth(sWord);
						// если слово последнее в строке, перевести текущую позицию
						// в начало новой строки на дисплее
						if (mdis.b_endline) { y+=font.getHeight(); x=0; }
					}
					// слово не входит по ширине с текущей позиции
					else {
						// перейти на новую строку
						y+=font.getHeight();
						// если на экране есть место для новой строки. отобразить слово с начала строки
						if(y+font.getHeight()<gh)
							g.drawString(sWord, 0,y, g.TOP|g.LEFT);
						// перевести текущую позицию на ширину слова
						x=font.stringWidth(sWord);
					}
					// повторять до тех пор, пока есть место для новой строки или не кончится файл
				} while(y+font.getHeight()<gh && !mdis.b_endFile);
				int index; // смещение начала страницы
				// если цикл закончился чтением слова, которое не было
				// отображено на экране, сместить начало новой страницы назад на одно слово
				if(mdis.b_endLine) index=offset; else index=offset-sWord.length();
				// поместить в стек начало следующей страницы
				if(!mdis.b_endFile) PageIndex.push(new Integer(index));
				// закрыть поток
				try{
					mdis.close();
				} catch(IOException ioe) {}
					// метод обработки нажатий клавиш
		}
		public void keyPressed(int keyCode){
			// обработка нажатий клавиш
			switch(keyCode) {
				case KEY_NUM1:
					// удалить из стека начала следующей и текущей страницы
					PageIndex.pop();
					if(!PageIndex.empty()) Pagelndex.pop();
					repaint();
					break;
				case KEY_NUM2:
					// перерисовать экран (начало следующей страницы уже в стеке)
					repaint();
					break;
				case KEY_NUM0:
					// очистить стек и перерисовать экран
					PageIndex.removeAl1Elements();
					repaint();
					break;
			}
		}
	} // class BookCanvas
} //class BookReader
