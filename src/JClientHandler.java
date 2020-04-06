
import java.io.*;
import java.net.Socket;

import java.util.Scanner;
import java.util.ArrayList;

public class JClientHandler implements Runnable {
	private volatile static int round = 1;
	private Socket connectionSock = null;
	private ArrayList<Socket> socketList;
	//HashMap<String,Integer> scoree = new HashMap<String, Integer>();
	private ArrayList<Integer> scores;
	private volatile static ArrayList<String> playerNames;
public volatile static int clientNum = 0;
	private int individualClientNum;
	//public int state = 0;
//	public volatile static boolean firstBuzzClientAnswered = false;
	private volatile static ArrayList<Socket> buzzIn;
	private volatile static int clientToAnswer = 0;
	//	private volatile static boolean wasCorrect = false;
	String[] OSQuestionsArray = new String[2];
	String[] OSKeysArray = new String[2];
	String[] CCNQuestionsArray = new String[2];
	String[] CCNKeysArray = new String[2];
	private volatile int countOS = 0;
	private volatile int countCCN = 0;
	private volatile int totalQuestionCount = 1;
//	boolean playerOne[][] = new boolean[2][2]; //2-D array because [Category][Question].. 4 questions total
//	boolean playerTwo[][] = new boolean[2][2];
	String name = "";


	//private int currentQuestion = 0;
	private volatile static String one = "";
	private volatile static String two = "";
	private String buzzer[] = new String[2];
	private String osq1ans;
	//private volatile static String three = "";
	BufferedReader clientInput[] = new BufferedReader[2];
	DataOutputStream clientOutput[] = new DataOutputStream[2];
	String inp;
	//String commonBuzzer;


	JClientHandler(Socket sock, ArrayList<Socket> socketList)
	{
		scores = new ArrayList<Integer>();
		playerNames = new ArrayList<String>();

		scores.add(0);
		scores.add(0);
		//	scores.add(0);
		this.connectionSock = sock;
		this.socketList = socketList;
		// Keep reference to master list
		//buzzIn = new ArrayList<Socket>();

	}


	public void run()
	{
		// Get data from a client and send it to everyone else
		try {

			System.out.println("Connection made with socket " + connectionSock);

			//BufferedReader clientInputAll = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
			//	BufferedReader clientInput[] = new BufferedReader[3];
			clientInput[0] = new BufferedReader(new InputStreamReader(socketList.get(0).getInputStream()));
			clientInput[1] = new BufferedReader(new InputStreamReader(socketList.get(1).getInputStream()));
			//	clientInput[2] = new BufferedReader(new InputStreamReader(socketList.get(2).getInputStream()));

			//DataOutputStream clientOutputAll = new DataOutputStream(connectionSock.getOutputStream());
			clientOutput[0] = new DataOutputStream(socketList.get(0).getOutputStream());
			clientOutput[1] = new DataOutputStream(socketList.get(1).getOutputStream());
			//	clientOutput[2] = new DataOutputStream(socketList.get(2).getOutputStream());


			//	boolean connected = false;

			String name = "";
		//	String buzzInStr = "";


			while (clientNum < 2)
			{
				name = clientInput[clientNum].readLine();
				playerNames.add(name);
				System.out.println("name: " + name);
				clientOutput[clientNum].writeBytes("You are contestant number: " + (clientNum + 1) + "\n");
				individualClientNum = (clientNum + 1);
				if (individualClientNum == 1)
					one = name;
				else if (individualClientNum == 2)
					two = name;
				//else if (individualClientNum == 3)
				//	three = name;
				clientNum++;

			}


			//String choice="";
			clientOutput[1].writeBytes("Player one is selecting category please wait \n");
			//clientOutput[2].writeBytes("Player one is selecting category please wait \n");

			clientOutput[0].writeBytes("Select the category \n1.OS\n2.CCN \n");
			inp = clientInput[0].readLine();


			Scanner OSQ = new Scanner(new File("OSQuiz.txt"));
			Scanner OSA = new Scanner(new File("OS_Keys.txt"));
			Scanner CCNQ = new Scanner(new File("CCNQuiz.txt"));
			Scanner CCNA = new Scanner(new File("CCN_Keys.txt"));
			int lineCount = 0;
			//String OSQline = "";
			//String OSAline = "";
			while (OSQ.hasNextLine())
			{

				OSQuestionsArray[lineCount] = OSQ.nextLine();
				OSKeysArray[lineCount] = OSA.nextLine();
				CCNQuestionsArray[lineCount] = CCNQ.nextLine();
				CCNKeysArray[lineCount] = CCNA.nextLine();
				lineCount++;
			}


			while (totalQuestionCount <=4)
			{


				if (inp.equals("1") && (countOS <= 1))// &&osCount<2
				{


					if (countOS == 0)
					{

						clientOutput[1].writeBytes("Player one has selected OS \n");
						//clientOutput[2].writeBytes("Player one has selected OS \n");
					}


					//	System.out.println(OSQuestionsArray[0]+"pritn");

					//for (Socket s : socketList) {
					//clientOutputAll = new DataOutputStream(s.getOutputStream());

					clientOutput[0].writeBytes("\n" + OSQuestionsArray[countOS] + "\n \n");
					clientOutput[1].writeBytes("\n" + OSQuestionsArray[countOS] + "\n \n");

					//}

					//for (Socket s : socketList)

					//{
					//	clientOutputAll = new DataOutputStream(s.getOutputStream());
					clientOutput[0].writeBytes("\n Press Buzzer" + "\n \n"); //Buzzer is A, if A is pressed then second client must input key other than B
					clientOutput[1].writeBytes("\n Press Buzzer" + "\n \n"); //Buzzer is B, if B is pressed then second client must input key other than A


					//for(Socket s:socketList)

					buzzer[0] = clientInput[0].readLine();
					//String a=new String(String.valueOf(buzzer[0].charAt(0)));
					buzzer[1] = clientInput[1].readLine();
					//				commonBuzzer=clientInputAll.readLine();


					if (buzzer[0].equalsIgnoreCase("A") )					{
						clientOutput[0].writeBytes("You pressed the key first\n ");
						clientOutput[1].writeBytes(one + " pressed the key first\n");
						System.out.println(one + "Pressed the key first");
						clientOutput[0].writeBytes("Enter the answer \n");
						osq1ans = clientInput[0].readLine();
						if (osq1ans.equalsIgnoreCase(OSKeysArray[countOS]))
						{

							clientOutput[0].writeBytes("Correct \n");
							System.out.println("Correct Answer ");


							int currentOne = scores.get(0);
							//int currentTwo =scores.get(1)
							scores.set(0, currentOne + 10);


							//scores.add(0, +10);
							//scores.add(1, 0);


							//for(Socket s:socketList)

							clientOutput[0].writeBytes("\n *****Score after round " + round + " is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
							clientOutput[1].writeBytes("\n ***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
							round++;
							countOS++;
							totalQuestionCount++;
							//playerOne[0][0] = true; //next will be selected by P1
							//playerTwo[0][0] = false;

							clientOutput[0].writeBytes("Select the category \n1.OS\n2.CCN \n");
							clientOutput[1].writeBytes("Player one is selecting category please wait \n");
							inp = clientInput[0].readLine();

							if (countOS<=1 && inp.equalsIgnoreCase("1")) //OS AGAIN
								continue;
							if (countCCN<=1 && inp.equalsIgnoreCase("2")) //CCN
								continue;
							if (countOS ==1 && countCCN<=1)
							{
								clientOutput[1].writeBytes("Redirecting to CCN \n  \n");

								clientOutput[0].writeBytes("Redirecting from CCN \n");
								inp="2";
								continue;
							}



							else
							{
								clientOutput[1].writeBytes("OS questions are completed \n");

								clientOutput[0].writeBytes("OS questions are completed \n");
								inp="2";
								continue;
							}




						}

						else
						//if ans is wrong
						{

							clientOutput[0].writeBytes("\n That is Incorrect\n");

							clientOutput[1].writeBytes("\n That is Incorrect\n");

							int currentOne = scores.get(0);
							scores.set(0, currentOne - 10);

							//scores.add(0, -10);
							//scores.add(1, 0);
							clientOutput[0].writeBytes("\n***** Score after round " + round + " is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
							clientOutput[1].writeBytes("\n***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
							round++;
							countOS++;
							totalQuestionCount++;

							if (countOS ==1 && countCCN<=1)
							{
								clientOutput[1].writeBytes("Redirecting to CCN \n  \n");

								clientOutput[0].writeBytes("Redirecting from CCN \n");
								inp="2";
								continue;
							}

							if (countOS == 1)
							{
								clientOutput[0].writeBytes("Next question from OS \n");
								clientOutput[1].writeBytes("Next question from OS \n");
								inp = "1";
								continue;

							}



						/*	else if (countOS == 2)
							{
									clientOutput[0].writeBytes("No more questions from OS \n");
									clientOutput[1].writeBytes("No more questions from OS");
									break;
								}*/

							//playerOne[0][0] = false;
							//playerTwo[0][0] = true;//next will be selected by P2

						}

					}

					if (buzzer[1].equalsIgnoreCase("B"))
					{


						clientOutput[1].writeBytes("You pressed the key first\n ");
						clientOutput[0].writeBytes(two + " pressed the key first\n");
						System.out.println(two + "Pressed the key first");
						clientOutput[1].writeBytes("Enter the answer\n");
						osq1ans = clientInput[1].readLine();
						if (osq1ans.equalsIgnoreCase(OSKeysArray[countOS])) {
							clientOutput[1].writeBytes("Correct \n");
							int currentOne = scores.get(1);

							scores.set(1, currentOne + 10);
							//scores.add(0, 0);
							//scores.add(1, +10);

							clientOutput[0].writeBytes("\n***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
							clientOutput[1].writeBytes("\n***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
							countOS++;
							round++;
							totalQuestionCount++;
							//playerOne[0][0] = false;
							//playerTwo[0][0] = true;//next will be selected by P2
							clientOutput[1].writeBytes("Select the category \n1.OS\n2.CCN \n");
							clientOutput[0].writeBytes("Player two is selecting category please wait \n");
							inp = clientInput[1].readLine();


							if (countOS<=1 && inp.equalsIgnoreCase("1")) //OS AGAIN
								continue;
							if (countCCN<=1 && inp.equalsIgnoreCase("2")) //CCN
								continue;

							if (countOS ==1 && countCCN<=1)
							{
								clientOutput[1].writeBytes("Redirecting to CCN \n  \n");

								clientOutput[0].writeBytes("Redirecting from CCN \n");
								inp="2";
								continue;
							}



							else
							{
								clientOutput[0].writeBytes("No more questions from OS \n");
								clientOutput[1].writeBytes("No more questions from OS \n");
								inp="2";
								continue;
							}

						/*	if (countOS<=1)
								continue;
							else
							{
								clientOutput[0].writeBytes("No more questions from OS \n");
								clientOutput[1].writeBytes("No more questions from OS \n");
								inp="2";
								continue;
							}
*/

						}
						else {
							//for(Socket s:socketList)

							clientOutput[0].writeBytes("\n That is Incorrect\n");
							clientOutput[1].writeBytes("\n That is Incorrect\n");
							int currentOne = scores.get(1);

							scores.set(1, currentOne - 10);
							clientOutput[0].writeBytes("\n***** Score after round " + round + " is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
							clientOutput[1].writeBytes("\n***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "******\n");
							round++;
							totalQuestionCount++;
							countOS++;

							if (countOS == 1)
							{

								inp = "1";
								clientOutput[0].writeBytes("Next question from OS \n");
								clientOutput[1].writeBytes("Next question from OS");
								continue;

							}
							if (countOS ==1 && countCCN<=1)
							{
								clientOutput[1].writeBytes("Redirecting to CCN \n  \n");

								clientOutput[0].writeBytes("Redirecting from CCN \n");
								inp="2";
								continue;
							}

							/*else if (countOS == 2) {
								clientOutput[0].writeBytes("No more questions from OS \n");
								clientOutput[1].writeBytes("No more questions from OS");
								break;
							}*/


							//playerOne[0][0] = true;//next will be selected by P1
							//playerTwo[0][0] = false;


						}
					}
				}






					//countOS++; //enable this and below later if necessary
					//totalQuestionCount++;
					//break;


					if (inp.equals("2") && countCCN <= 1)// &&osCount<2
					{
						if (countCCN == 1)
						{

							clientOutput[1].writeBytes("Player one has selected CCN \n");
							//clientOutput[2].writeBytes("Player one has selected OS \n");

						}
						//	System.out.println(OSQuestionsArray[0]+"pritn");

						//for (Socket s : socketList) {
						//clientOutputAll = new DataOutputStream(s.getOutputStream());

						clientOutput[0].writeBytes("\n" + CCNQuestionsArray[countCCN] + "\n \n");
						clientOutput[1].writeBytes("\n" + CCNQuestionsArray[countCCN] + "\n \n");

						//}

						//for (Socket s : socketList)

						//{
						//	clientOutputAll = new DataOutputStream(s.getOutputStream());
						clientOutput[0].writeBytes("\n Press buzzer" + "\n \n");
						clientOutput[1].writeBytes("\n Press buzzer" + "\n \n");


						//for(Socket s:socketList)

						buzzer[0] = clientInput[0].readLine();
						//String a=new String(String.valueOf(buzzer[0].charAt(0)));
						buzzer[1] = clientInput[1].readLine();

						String ccnq1ans;
						if (buzzer[0].equalsIgnoreCase("B") )
						{
							clientOutput[0].writeBytes("You pressed the key first\n ");
							clientOutput[1].writeBytes(one + " pressed the key first\n");
							System.out.println(one + "Pressed the key first");
							clientOutput[0].writeBytes("Enter the answer \n");
							ccnq1ans = clientInput[0].readLine();
							if (ccnq1ans.equalsIgnoreCase(CCNKeysArray[countCCN]))
							{

								clientOutput[0].writeBytes("Correct \n");
								int scoreOne = scores.get(0);
								scores.set(0, scoreOne + 10);
								//	scores.add(0, +10);
								//	scores.add(1, 0);


								//for(Socket s:socketList)

								clientOutput[0].writeBytes("\n***** Score after round " + round + " is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
								clientOutput[1].writeBytes("\n***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "******\n");
								round++;
								countCCN++;
								totalQuestionCount++;

								//		playerOne[1][0] = true; //next will be selected by P1
								//		playerTwo[1][0] = false;

								clientOutput[0].writeBytes("Select the category \n1.OS\n2.CCN \n");
								clientOutput[1].writeBytes("Player one is selecting category please wait \n");
								inp=clientInput[0].readLine();
								if (countOS<=1 && inp.equalsIgnoreCase("1")) //OS AGAIN
									continue;
								if (countCCN<=1 && inp.equalsIgnoreCase("2")) //CCN
									continue;
								if (countCCN ==1 && countOS<=1)
								{
									clientOutput[1].writeBytes("Redirecting to OS \n  \n");

									clientOutput[0].writeBytes("Redirecting from OS \n");
									inp="1";
									continue;
								}




								else
								{
									clientOutput[0].writeBytes("No more questions from CCN \n");
									clientOutput[1].writeBytes("No more questions from CCN \n");
									inp="1";
									continue;
								}





							}
							else

								{

								clientOutput[0].writeBytes("\n That is incorrect\n");

								clientOutput[1].writeBytes("\n That is incorrect\n");
								//scores.add(0, -10);
								//scores.add(1, 0);
								int scoreOne = scores.get(0);
								scores.set(0, scoreOne - 10);
								clientOutput[0].writeBytes("\n***** Score after round " + round + " is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
								clientOutput[1].writeBytes("\n***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
								round++;
								countCCN++;
								totalQuestionCount++;
								if (countCCN ==1 && countOS<=1)
									{
										clientOutput[1].writeBytes("Redirecting to OS \n  \n");

										clientOutput[0].writeBytes("Redirecting from OS \n");
										inp="1";
										continue;
									}

								if (countCCN == 1)
								{
									clientOutput[0].writeBytes("Next CCN question is  \n");
									clientOutput[1].writeBytes("Next CCN question is  \n");
									inp = "2";
									continue;

								}

								//playerOne[1][0] = false;
								//playerTwo[1][0] = true;//next will be selected by P2

							}

						}

						if (buzzer[1].equalsIgnoreCase("B") ) {


							clientOutput[1].writeBytes("You pressed the key first\n ");
							clientOutput[0].writeBytes(two + " pressed the key first\n");
							System.out.println(two + "Pressed the key first");
							clientOutput[1].writeBytes("Enter the answer \n");
							ccnq1ans = clientInput[1].readLine();
							if (ccnq1ans.equalsIgnoreCase(CCNKeysArray[countCCN])) {
								clientOutput[1].writeBytes("Correct \n");
								int scoreOne = scores.get(1);
								scores.set(1, scoreOne + 10);
								//scores.add(1, +10);
								//scores.add(0, 0);

								clientOutput[0].writeBytes("\n***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
								clientOutput[1].writeBytes("\n***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
								countCCN++;
								round++;
								totalQuestionCount++;
								//	playerOne[1][0] = false;
								//	playerTwo[1][0] = true;//next will be selected by P2

								clientOutput[1].writeBytes("Select the category \n1.OS\n2.CCN \n");
								clientOutput[0].writeBytes("Player two is selecting category please wait \n");
								inp=clientInput[1].readLine();
								if (countOS<=1 && inp.equalsIgnoreCase("1")) //OS AGAIN
									continue;
								if (countCCN<=1 && inp.equalsIgnoreCase("2")) //CCN
									continue;
								if (countCCN ==1 && countOS<=1)
								{
									clientOutput[1].writeBytes("Redirecting to OS \n  \n");

									clientOutput[0].writeBytes("Redirecting from OS \n");
									inp="1";
									continue;
								}



								else
								{
									clientOutput[0].writeBytes("No more questions from CCN \n");
									clientOutput[1].writeBytes("No more questions from CCN \n");
									inp="1";
									continue;
								}




							} else
								{
								//for(Socket s:socketList)

								clientOutput[0].writeBytes("\n That is incorrect\n");
								clientOutput[1].writeBytes("\n That is incorrect\n");

								int countOne = scores.get(1);
								scores.set(1, countOne - 10);

								//	scores.add(0, 0);
								//	scores.add(1, -10);
								clientOutput[0].writeBytes("\n***** Score after round " + round + " is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
								clientOutput[1].writeBytes("\n***** Score after round " + round + "is \n  " + one + " = " + scores.get(0) + "\n " + two + " =" + scores.get(1) + "*****\n");
								round++;
								totalQuestionCount++;
								countCCN++;
									if (countCCN ==1 && countOS<=1)
									{
										clientOutput[1].writeBytes("Redirecting to OS \n  \n");

										clientOutput[0].writeBytes("Redirecting from OS \n");
										inp="1";
										continue;
									}

								if (countCCN == 1)
								{
									clientOutput[0].writeBytes("Next CCN question is  \n");
									clientOutput[1].writeBytes("Next CCN question is  \n");
									inp = "2";
									continue;

								}




								//	playerOne[1][0] = true;//next will be selected by P1
								//	playerTwo[1][0] = false;

							}

						}


						//countOS++; //enable this and below later if necessary
						//totalQuestionCount++;
						//break;


					}

				/*	else if (countCCN ==2)
					{
						clientOutput[0].writeBytes("No more questions from CCN After \n");
						clientOutput[1].writeBytes("No more questions from CCN After\n");
						break;


					}*/

			}



			}
		catch (Exception e) {
			System.out.println(e.toString());
		}

		try{
		if (scores.get(0) > scores.get(1))
		{
			clientOutput[0].writeBytes("Congratulations !! You have won \n");
			clientOutput[1].writeBytes("Better Luck Next \n");
			System.out.println(one+"has won the game ");



		}else if (scores.get(1) >scores.get(0) )
		{

			clientOutput[1].writeBytes("Congratulations !! You have won \n");
			clientOutput[0].writeBytes("Better Luck Next \n");
			System.out.println(two+"has won the game ");

		}
		else {
			clientOutput[0].writeBytes("DRAW \n");
			clientOutput[1].writeBytes("DRAW\n");
			System.out.println("DRAW ");
		}
		}catch (Exception e)
		{
		System.out.println(e.toString());
		}


		}
	}






























