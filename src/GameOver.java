import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;

public class GameOver extends JFrame {

	private JPanel contentPane;
	
	public GameOver(GUI gui)
	{
		setType(Type.UTILITY);
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("GAME OVER");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRestart = new JButton("RESTART");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restart(gui);
			}
		});
		btnRestart.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnRestart.setBounds(33, 170, 140, 80);
		contentPane.add(btnRestart);
		
		JButton btnNewGame = new JButton("NEW GAME");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame(gui);
			}
		});
		btnNewGame.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewGame.setBounds(253, 170, 140, 80);
		contentPane.add(btnNewGame);
		
		JLabel lblTimetaken = new JLabel("Time Taken");
		lblTimetaken.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimetaken.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTimetaken.setBounds(33, 46, 140, 45);
		contentPane.add(lblTimetaken);
		
		JLabel lblTimeTakenAnswer = new JLabel(gui.sec+" seconds");
		lblTimeTakenAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeTakenAnswer.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTimeTakenAnswer.setBounds(253, 46, 140, 45);
		contentPane.add(lblTimeTakenAnswer);
		
		JLabel lblBestTime = new JLabel("Best Time");
		lblBestTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblBestTime.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBestTime.setBounds(33, 91, 140, 58);
		contentPane.add(lblBestTime);
		
		JLabel lblBestTimeAnswer = new JLabel(highScore(gui.difficulty,gui.sec,gui.gameState)+" seconds");
		lblBestTimeAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		lblBestTimeAnswer.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBestTimeAnswer.setBounds(253, 91, 140, 58);
		contentPane.add(lblBestTimeAnswer);
		
		JLabel lblNewLabel = new JLabel("Congratulations!!! You have Won!!!");
		lblNewLabel.setFont(new Font("Perpetua Titling MT", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 424, 31);
		contentPane.add(lblNewLabel);
		
	}
	
	private String highScore(char diff,int score,char gameState)
	{
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(ResourceLoader.read("highScore.txt")));
			
			int scores[]=new int[3];
			scores[0]=Integer.parseInt(br.readLine());
			scores[1]=Integer.parseInt(br.readLine());
			scores[2]=Integer.parseInt(br.readLine());
			
			br.close();
			
			if(gameState=='w')
			{
				switch(diff)
				{
					case 'e':
						if(score<scores[0] || scores[0]==0) scores[0]=score;
						break;
					case 'm':
						if(score<scores[1] || scores[1]==0) scores[1]=score;
						break;
					case 'h':
						if(score<scores[2] || scores[2]==0) scores[2]=score;
						break;
				}
			
				FileWriter fw=new FileWriter("highScore.txt",false);
				BufferedWriter bw=new BufferedWriter(fw);
				PrintWriter pw=new PrintWriter(bw);
				
				pw.println(scores[0]);
				pw.println(scores[1]);
				pw.println(scores[2]);
				
				pw.close();bw.close();fw.close();
			
			}
			
			if(diff=='e' && scores[0]!=0) return scores[0]+"";
			if(diff=='m' && scores[1]!=0) return scores[1]+"";
			if(diff=='h' && scores[2]!=0) return scores[2]+"";
			
		}
		catch (IOException e)
		{
			System.out.println("File Not Found");
		}
		return "(N.A.)";
	}
	
	private void restart(GUI gui)
	{
		this.dispose();
		gui.restart();
	}
	
	private void newGame(GUI gui)
	{
		this.dispose();
		gui.newGame();
	}
}
