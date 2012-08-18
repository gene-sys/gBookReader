import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import Java.util.Stack;
import java.io.InputStream;
import java.io.EOFException;
import java.io.IOException;
public class gBookReader extends MIDlet {
	private Display display; // �������� �������
	private BookCanvas fCanvas; // �������� �������� �����

	public void destroyApp(boolean flag) {
	}
	public void pauseApp() {}
	// ��������� ����� �������
	public void startApp() {
		// �������� ������ �� �������� �������
		display = Display.getDisplay(this);
		// ������� ����� ����� � ������ ��������� ������
		Form form = new Form(getClass().getName());
		// ������� ������ ��������� ��������� �����
		fCanvas =new BookCanvas();
		// ���������� �������� �������� �����
		display.setCurrent(fCanvas);
	}

	// ����� ��������� ��������� �����
	public class BookCanvas extends Canvas {
		private Stack PageIndex; // ���� �������� ������� �� ������ ������
		public BookCanvas() {
			// ������� ����� ������ ����� ������� �������
			PageIndex = new Stack();
		}
		// ����� ����������� ������� ��������� ���������
		protected void paint(Graphics g) {
			// �������� ������ � ������ ������� ������� ������
			int gw = g.getClipWidth();
			int gh = g.getClipHeight();
			// ������� �����
			Font font = Font.getFont(Font.FACE_MONOSPACE,
									Font.STYLE_PLAIN,
									Font.SIZE_SMALL);
			// ���������� �����
			g.setFont(font);
			// ���������������� �����
			InputStream is = getClass().getResourceAsStream("/story.txt");
			MyDataInputStream mdis = new MyDataInputStream(is);
			int x=0,y=0; // ������� ��������� ������ �� ������
			int offset=0; // �������� �� ��������� ������
			// ������� ������
			g.setColor(255,255,255);
			g.fillRect(0,0,gw,gh);
			g.setColor(0,0,0);
			// �������� �������� ��� ��������� ��������
			if(!PageIndex.empty())
				offset=((Integer)PageIndex.peek()).intValue();
				try{
					// ���������� �� ������ �� ����������� ��������
					mdis.skipBytes(offset);
				} catch(IOException ioe) {}
				String sWord;
				do {
					// ��������� ��������� ����� �� ������
					sWord = mdis.readWord();
					// ���������� �������� �� ����� �� ���� �����
					offset+=sWord.length();
					// ���� ������� ������� � ���������� ������ ����� �� ������� �� �����
					if(x+font.stringWidth(sWord)<=gw) {
						// ���������� �����, ������� � ������� �������
						g.drawString(sWord, x,y, g.TOP|g.LEFT);
						// �������� ������� ������� �� ���������� ������ �����
						x+=font.stringWidth(sWord);
						// ���� ����� ��������� � ������, ��������� ������� �������
						// � ������ ����� ������ �� �������
						if (mdis.b_endline) { y+=font.getHeight(); x=0; }
					}
					// ����� �� ������ �� ������ � ������� �������
					else {
						// ������� �� ����� ������
						y+=font.getHeight();
						// ���� �� ������ ���� ����� ��� ����� ������. ���������� ����� � ������ ������
						if(y+font.getHeight()<gh)
							g.drawString(sWord, 0,y, g.TOP|g.LEFT);
						// ��������� ������� ������� �� ������ �����
						x=font.stringWidth(sWord);
					}
					// ��������� �� ��� ���, ���� ���� ����� ��� ����� ������ ��� �� �������� ����
				} while(y+font.getHeight()<gh && !mdis.b_endFile);
				int index; // �������� ������ ��������
				// ���� ���� ���������� ������� �����, ������� �� ����
				// ���������� �� ������, �������� ������ ����� �������� ����� �� ���� �����
				if(mdis.b_endLine) index=offset; else index=offset-sWord.length();
				// ��������� � ���� ������ ��������� ��������
				if(!mdis.b_endFile) PageIndex.push(new Integer(index));
				// ������� �����
				try{
					mdis.close();
				} catch(IOException ioe) {}
					// ����� ��������� ������� ������
		}
		public void keyPressed(int keyCode){
			// ��������� ������� ������
			switch(keyCode) {
				case KEY_NUM1:
					// ������� �� ����� ������ ��������� � ������� ��������
					PageIndex.pop();
					if(!PageIndex.empty()) Pagelndex.pop();
					repaint();
					break;
				case KEY_NUM2:
					// ������������ ����� (������ ��������� �������� ��� � �����)
					repaint();
					break;
				case KEY_NUM0:
					// �������� ���� � ������������ �����
					PageIndex.removeAl1Elements();
					repaint();
					break;
			}
		}
	} // class BookCanvas
} //class BookReader
