package gui;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;

import common.*;
import messages.*;
import network.CameraServer;
import network.NetworkServer;

//import javax.swing.Timer;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;
import java.awt.Label;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Panel;
import java.awt.Canvas;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame {

	/*
	 * Small class to handle constantly updating the robot data boxes
	 */


	public static class RobotDataUpdateTask extends TimerTask {
		public RobotDataUpdateTask() {
		};

		public void run() {
			updateRobotDataBoxes();

		}
	}


	@SuppressWarnings("serial")
	public class RoundJTextField extends JTextField {
		private Shape shape;
		public RoundJTextField(int size) {
			super(size);
			setOpaque(false); // As suggested by @AVD in comment.
		}
		protected void paintComponent(Graphics g) {
			g.setColor(getBackground());
			g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 5, 5);
			super.paintComponent(g);
		}
		protected void paintBorder(Graphics g) {
			g.setColor(getForeground());
			g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 5, 5);
		}
		public boolean contains(int x, int y) {
			if (shape == null || !shape.getBounds().equals(getBounds())) {
				shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 5, 5);
			}
			return shape.contains(x, y);
		}
	}

	private static RobotData robotData = new RobotData();
	public static MessageQueue messageQueue = new MessageQueue();
//	private static MessageQueue pingMessageQueue = new MessageQueue();
	public static RecoveryStack recoveryStack = new RecoveryStack();
//	MessageQueue.


	private NetworkServer server = new NetworkServer(11000, messageQueue, robotData, recoveryStack);
	private boolean runServer = false;

	private static DefaultListModel<String> model = new DefaultListModel<String>();
	private static DefaultListModel<String> recovery_model = new DefaultListModel<String>();
	public static JList messageList = new JList<String>(model);
	public static Message already_ran_recovery_message;
	private MessageType selectedMessageType = MessageType.MSG_STOP;
	private Message selectedMessage = new MsgStop();
	private JLabel[] messageLabels = new JLabel[8];

	private JFrame frame;
	private JTextField tbox_messageAddPosition;
	private JTextField tbox_data0;
	private JTextField tbox_data1;
	private JTextField tbox_data2;
	private JTextField tbox_data3;
	private JTextField tbox_data4;
	private JTextField tbox_data5;
	private JTextField tbox_data6;
	private JTextField tbox_data7;
	public static JList<String> list_1;
	private static JTextField tbox_leftMotorID;
	private static JTextField tbox_leftMotorCurrent;
	private static JTextField tbox_leftMotorVoltage;
	private static JTextField tbox_leftMotorTemperature;
	private static JTextField tbox_leftMotorMode;
	private static JTextField tbox_leftMotorSetpoint;
	private static JTextField tbox_leftMotorPosition;
	private static JTextField tbox_leftMotorSpeed;
	private static JTextField tbox_leftMotorFLimit;
	private static JTextField tbox_leftMotorRLimit;
	private static JTextField tbox_rightMotorID;
	private static JTextField tbox_rightMotorCurrent;
	private static JTextField tbox_rightMotorVoltage;
	private static JTextField tbox_rightMotorTemperature;
	private static JTextField tbox_rightMotorMode;
	private static JTextField tbox_rightMotorSetpoint;
	private static JTextField tbox_rightMotorPosition;
	private static JTextField tbox_rightMotorSpeed;
	private static JTextField tbox_rightMotorFLimit;
	private static JTextField tbox_rightMotorRLimit;
	private static JTextField tbox_scoopMotorID;
	private static JTextField tbox_scoopMotorCurrent;
	private static JTextField tbox_scoopMotorVoltage;
	private static JTextField tbox_scoopMotorTemperature;
	private static JTextField tbox_scoopMotorMode;
	private static JTextField tbox_scoopMotorSetpoint;
	private static JTextField tbox_scoopMotorPosition;
	private static JTextField tbox_scoopMotorSpeed;
	private static JTextField tbox_scoopMotorFLimit;
	private static JTextField tbox_scoopMotorRLimit;
	private static JTextField tbox_depthMotorID;
	private static JTextField tbox_depthMotorCurrent;
	private static JTextField tbox_depthMotorVoltage;
	private static JTextField tbox_depthMotorTemperature;
	private static JTextField tbox_depthMotorMode;
	private static JTextField tbox_depthMotorSetpoint;
	private static JTextField tbox_depthMotorPosition;
	private static JTextField tbox_depthMotorSpeed;
	private static JTextField tbox_depthMotorFLimit;
	private static JTextField tbox_depthMotorRLimit;
	private static JTextField tbox_winchMotorID;
	private static JTextField tbox_winchMotorCurrent;
	private static JTextField tbox_winchMotorVoltage;
	private static JTextField tbox_winchMotorTemperature;
	private static JTextField tbox_winchMotorMode;
	private static JTextField tbox_winchMotorSetpoint;
	private static JTextField tbox_winchMotorPosition;
	private static JTextField tbox_winchMotorSpeed;
	private static JTextField tbox_winchMotorFLimit;
	private static JTextField tbox_winchMotorRLimit;


	private static JTextField tbox_imuData;
	private static JTextField tbox_cameraAngleData1;
	private static JLabel hbLabel;
	private static JLabel lblBumpLeft;
	private static JLabel lblBumpRight;

	private static ImagePanel imagepanel = new ImagePanel();
	private static JTextField tbox_cameraAngleData2;
	private static JTextField tbox_IRData;
	public static double pose;


	public static void main(String[] args) {
		// MessageQueue messageQueue = new MessageQueue();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//******************CALL THE FUNCTION BELOW FOR YOUR OWN STATE MACHINE*********************//
					updateAutonomyQueue(messageQueue);
					//*****************************************************************************************//
					Timer simpleTimer = new Timer();
					Thread cameraServerThread = new Thread(new CameraServer(imagepanel));
					cameraServerThread.start();

					GUI window = new GUI(messageQueue);
					simpleTimer.scheduleAtFixedRate(new RobotDataUpdateTask(), 1000, 500);
//                    simpleTimer.scheduleAtFixedRate(new ModelUpdate(), 1000, 500);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initialize(MessageQueue messageQueue) {

		this.messageQueue = messageQueue;

		frame = new JFrame();
		frame.setBounds(0, 0, 1000, 700); //1600,900
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setTitle("IBEX Control Station");
		setSize(new Dimension(1300, 822));
		setResizable(false);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setMaximumSize(new Dimension(785, 32767));

//		JScrollPane panel_1 = new JScrollPane();
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		panel_1.setAutoscrolls(true);
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setForeground(Color.WHITE);
		
		JButton pingButton = new JButton("Ping");
		pingButton.setBackground(Color.GRAY);
		pingButton.setForeground(Color.WHITE);
		pingButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		pingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		pingButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						messageQueue.clear();
						updatePingTestQueue(messageQueue);
						updateMessageQueueList(messageList);

						if (!runServer) {
							server.startServer();
						}
						runServer = true;

//						See the LED status
//						while ((robotData.getLEDSensor().getValue().intValue()>0) && (!isLEDOn))

						while(true) //was LED ever on rather than getting the real time status of the LED
						{
							if (robotData.getLEDSensor().getValue().intValue() == 0){
								pingButton.setBackground(new Color(100, 149, 237));
								break;
							}
							else if (robotData.getLEDSensor().getValue().intValue() == 1){
								pingButton.setBackground(Color.GRAY);
								break;
							}
							
							messageQueue.clear();
							updateMessageQueueList(messageList);
							
							
							System.out.println("GOT RESPONSE");
						}
						


//						System.out.println("*******LED inside*******");
//							System.out.println((robotData.getLEDSensor().getValue().intValue()));
//						try{
//							TimeUnit.MICROSECONDS.sleep(1);
//						}
//						catch (Exception timer_exception){
//							System.out.print(timer_exception);
//						}
//
//						System.out.println("Outside");
//						if (isLEDOn){
//							pingButton.setBackground(new Color(100, 149, 237));
//							messageQueue.clear();
//							updateAutonomyQueue(messageQueue);
//							updateMessageQueueList(messageList);
//						}
					}
		});
		
				JButton btnClearAll = new JButton("Teleop");//"Clear all"
				btnClearAll.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					}
				});
				btnClearAll.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						messageQueue.clear();
						Message msg_teleop = new MsgMotorValues();
						messageQueue.addAtFront(msg_teleop);
						btnClearAll.setBackground(new Color(255, 165, 0));

						updateMessageQueueList(messageList);
						updateRecoveryStackList(list_1);
						selectedMessage = MessageFactory.makeMessage(selectedMessageType);

					}
				});
				btnClearAll.setBackground(Color.GRAY);
				btnClearAll.setForeground(new Color(255, 255, 255));
				btnClearAll.setFont(new Font("Tahoma", Font.BOLD, 20));
		
				JButton btnStartQueue = new JButton("START");
				btnStartQueue.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
//						messageQueue.clear();
//						updateAutonomyQueue(messageQueue);
						updateMessageQueueList(messageList);
						if (!runServer) {
							server.startServer();
						}
						runServer = true;
					}
				});
				btnStartQueue.setFont(new Font("Tahoma", Font.BOLD, 20));
				btnStartQueue.setBackground(new Color(154, 205, 50));
				btnStartQueue.setForeground(new Color(255, 255, 255));
		
				JButton btnStop = new JButton("STOP");
				btnStop.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btnStop.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
//						if (runServer) {
//							try {
//								server.stopServer();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//						runServer = false;
//						runRecovery();

						Message stopMsgClose = new MsgStop();
						messageQueue.clear();
						messageQueue.addAtFront(stopMsgClose);
						updateMessageQueueList(messageList);
						updateRecoveryStackList(list_1);
					}
				});
				btnStop.setFont(new Font("Tahoma", Font.BOLD, 20));
				btnStop.setForeground(new Color(255, 255, 255));
				btnStop.setBackground(new Color(205, 92, 92));
		
		hbLabel = new JLabel("HB");
		hbLabel.setOpaque(true);
		hbLabel.setBackground(Color.GRAY);
		hbLabel.setForeground(Color.WHITE);
		hbLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hbLabel.setFont(new Font("Tahoma", Font.BOLD, 20));


		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 787, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(btnStartQueue, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(hbLabel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(pingButton, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnStop, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
										.addComponent(btnClearAll, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
								.addComponent(imagepanel, GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)))
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 1274, Short.MAX_VALUE)
						.addComponent(lblStatus))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(35)
							.addComponent(imagepanel, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(hbLabel, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
									.addComponent(pingButton, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
								.addComponent(btnClearAll, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnStop, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
								.addComponent(btnStartQueue, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblStatus)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		list_1 = new JList<String>(recovery_model);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
//		panel_1.setAutoscrolls(true);
//		list_1.setAutoscrolls(true);
		gl_panel_1.setHorizontalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
								.addContainerGap()
								.addComponent(list_1, GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
								.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
								.addContainerGap()
								.addComponent(list_1, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(96, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
//		panel_1.setViewportView(list_1);


		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Main", null, panel_2, null);
		updateMessageQueueList(messageList);
		updateRecoveryStackList(list_1);

		JButton btnRemoveSelected = new JButton("Remove");
		btnRemoveSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRemoveSelected.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int removalIndex;
				try {
					removalIndex = messageList.getSelectedIndex();
					Message pop;
					pop = messageQueue.removeAtIndex(removalIndex);
					recoveryStack.addToStack(pop);
					updateMessageQueueList(messageList);
					updateRecoveryStackList(list_1);
				} catch (Exception exception) {
					System.out.println("Failed to remove message.");
				}

			}
		});
		btnRemoveSelected.setBackground(new Color(30, 144, 255));
		btnRemoveSelected.setForeground(new Color(255, 255, 255));
		btnRemoveSelected.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnRemoveSelected.setPreferredSize(new Dimension(100, 100));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(messageList, GroupLayout.PREFERRED_SIZE, 616, GroupLayout.PREFERRED_SIZE)
					.addGap(13)
					.addComponent(btnRemoveSelected, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(btnRemoveSelected, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
						.addComponent(messageList, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel_2.setLayout(gl_panel_2);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Commands", null, panel_3, null);
		panel_3.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(255, 222, 173));
		panel_5.setBounds(10, 11, 762, 558);
		panel_5.setLayout(null);

		JLabel lblCommandType = new JLabel("Command Type");
		lblCommandType.setBounds(10, 8, 140, 25);
		panel_5.add(lblCommandType);
		lblCommandType.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lbl_data0 = new JLabel("Data 0");
		lbl_data0.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_data0.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_data0.setBounds(10, 58, 168, 31);
		panel_5.add(lbl_data0);

		JLabel lbl_data1 = new JLabel("Data 1");
		lbl_data1.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_data1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_data1.setBounds(10, 100, 168, 31);
		panel_5.add(lbl_data1);

		JLabel lbl_data2 = new JLabel("Data 2");
		lbl_data2.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_data2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_data2.setBounds(10, 142, 168, 31);
		panel_5.add(lbl_data2);

		JLabel lbl_data3 = new JLabel("Data 3");
		lbl_data3.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_data3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_data3.setBounds(10, 184, 168, 31);
		panel_5.add(lbl_data3);

		JLabel lbl_data4 = new JLabel("Data 4");
		lbl_data4.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_data4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_data4.setBounds(10, 226, 168, 31);
		panel_5.add(lbl_data4);

		JLabel lbl_data5 = new JLabel("Data 5");
		lbl_data5.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_data5.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_data5.setBounds(10, 268, 168, 31);
		panel_5.add(lbl_data5);

		JLabel lbl_data6 = new JLabel("Data 6");
		lbl_data6.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_data6.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_data6.setBounds(10, 310, 168, 31);
		panel_5.add(lbl_data6);

		JLabel lbl_data7 = new JLabel("Data 7");
		lbl_data7.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_data7.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_data7.setBounds(10, 352, 168, 31);
		panel_5.add(lbl_data7);

		messageLabels[0] = lbl_data0;
		messageLabels[1] = lbl_data1;
		messageLabels[2] = lbl_data2;
		messageLabels[3] = lbl_data3;
		messageLabels[4] = lbl_data4;
		messageLabels[5] = lbl_data5;
		messageLabels[6] = lbl_data6;
		messageLabels[7] = lbl_data7;

		JComboBox<String> cbox_cmdType = new JComboBox<String>();
		cbox_cmdType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				MessageType newMessageType = MessageType.values()[cbox_cmdType.getSelectedIndex()];
				if (newMessageType != selectedMessageType) {
					selectedMessageType = newMessageType;
					selectedMessage = MessageFactory.makeMessage(selectedMessageType);
					updateMessageLabels();
					System.out.println("Selected a Message of Type: " + selectedMessageType.toString());
				}
			}
		});
		cbox_cmdType.setBounds(160, 5, 225, 31);
		setCommandComboBoxStrings(cbox_cmdType);

		panel_5.add(cbox_cmdType);
		cbox_cmdType.setFont(new Font("Tahoma", Font.PLAIN, 20));

		panel_3.add(panel_5);

		tbox_data0 = new JTextField();
		tbox_data0.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tbox_data0.setColumns(10);
		tbox_data0.setBounds(188, 58, 197, 31);
		panel_5.add(tbox_data0);

		tbox_data1 = new JTextField();
		tbox_data1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tbox_data1.setColumns(10);
		tbox_data1.setBounds(188, 100, 197, 31);
		panel_5.add(tbox_data1);

		tbox_data2 = new JTextField();
		tbox_data2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tbox_data2.setColumns(10);
		tbox_data2.setBounds(188, 142, 197, 31);
		panel_5.add(tbox_data2);

		tbox_data3 = new JTextField();
		tbox_data3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tbox_data3.setColumns(10);
		tbox_data3.setBounds(188, 184, 197, 31);
		panel_5.add(tbox_data3);

		tbox_data4 = new JTextField();
		tbox_data4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tbox_data4.setColumns(10);
		tbox_data4.setBounds(188, 226, 197, 31);
		panel_5.add(tbox_data4);

		tbox_data5 = new JTextField();
		tbox_data5.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tbox_data5.setColumns(10);
		tbox_data5.setBounds(188, 268, 197, 31);
		panel_5.add(tbox_data5);

		tbox_data6 = new JTextField();
		tbox_data6.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tbox_data6.setColumns(10);
		tbox_data6.setBounds(188, 310, 197, 31);
		panel_5.add(tbox_data6);

		tbox_data7 = new JTextField();
		tbox_data7.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tbox_data7.setColumns(10);
		tbox_data7.setBounds(188, 352, 197, 31);
		panel_5.add(tbox_data7);

		JButton btnAddToEnd = new JButton("Add to End");
		btnAddToEnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// array of pointers to text boxes so they can easily be
				// iterated over
				JTextField[] tboxes = new JTextField[8];
				tboxes[0] = tbox_data0;
				tboxes[1] = tbox_data1;
				tboxes[2] = tbox_data2;
				tboxes[3] = tbox_data3;
				tboxes[4] = tbox_data4;
				tboxes[5] = tbox_data5;
				tboxes[6] = tbox_data6;
				tboxes[7] = tbox_data7;
				Double[] tboxData = new Double[8];
				for (int i = 0; i < 8; i++) {
					try {
						tboxData[i] = Double.parseDouble(tboxes[i].getText());
					} catch (Exception exception) {
						tboxData[i] = 0.0;
					}
					selectedMessage.setDataByIndex(i, tboxData[i]);
				}
				messageQueue.addAtBack(selectedMessage);
				updateMessageQueueList(messageList);
				updateRecoveryStackList(list_1);
				selectedMessage = MessageFactory.makeMessage(selectedMessageType);
			}
		});
		btnAddToEnd.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnAddToEnd.setBounds(10, 394, 375, 41);
		panel_5.add(btnAddToEnd);

		JButton btnAddAtPosition = new JButton("Add at Position");
		btnAddAtPosition.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int messageIndex;
				try {
					messageIndex = Integer.parseInt(tbox_messageAddPosition.getText());
					messageQueue.addAtPosition(selectedMessage, messageIndex);

				} catch (Exception exception) {
					System.out.println("Could not add message at selected index, added to back of queue.");
					messageQueue.addAtBack(selectedMessage);
				}
  				updateMessageQueueList(messageList);
				updateRecoveryStackList(list_1);
				selectedMessage = MessageFactory.makeMessage(selectedMessageType);
			}
		});
		btnAddAtPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnAddAtPosition.setBounds(10, 446, 247, 58);
		panel_5.add(btnAddAtPosition);

		tbox_messageAddPosition = new JTextField();
		tbox_messageAddPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tbox_messageAddPosition.setBounds(267, 446, 118, 58);
		panel_5.add(tbox_messageAddPosition);
		tbox_messageAddPosition.setColumns(10);

		JPanel panel_robotData = new JPanel();
		panel_robotData.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Robot Data", null, panel_robotData, null);

		JPanel panel_leftMotorData = new JPanel();
		panel_leftMotorData.setBackground(new Color(205, 92, 92));

		JPanel panel_rightMotorData = new JPanel();
		panel_rightMotorData.setBackground(new Color(205, 92, 92));
		panel_rightMotorData.setLayout(null);

		JLabel lbl_rightMotorID = new JLabel("Device ID:");
		lbl_rightMotorID.setForeground(Color.WHITE);
		lbl_rightMotorID.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorID.setBounds(10, 51, 88, 14);
//		lbl_rightMotorID.setBackground(Color.ORANGE);
//		lbl_rightMotorID.setOpaque(true);
		panel_rightMotorData.add(lbl_rightMotorID);

		tbox_rightMotorID = new JTextField();
		tbox_rightMotorID.setColumns(10);
		tbox_rightMotorID.setBounds(108, 48, 86, 20);
//		tbox_rightMotorID.setBackground(Color.ORANGE);
//		tbox_rightMotorID.setOpaque(true);
		panel_rightMotorData.add(tbox_rightMotorID);

		JLabel lbl_rightMotorCurrent = new JLabel("Current (A):");
		lbl_rightMotorCurrent.setForeground(Color.WHITE);
		lbl_rightMotorCurrent.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorCurrent.setBounds(10, 76, 88, 14);
		panel_rightMotorData.add(lbl_rightMotorCurrent);

		tbox_rightMotorCurrent = new JTextField();
		tbox_rightMotorCurrent.setColumns(10);
		tbox_rightMotorCurrent.setBounds(108, 73, 86, 20);
		panel_rightMotorData.add(tbox_rightMotorCurrent);

		tbox_rightMotorVoltage = new JTextField();
		tbox_rightMotorVoltage.setColumns(10);
		tbox_rightMotorVoltage.setBounds(108, 126, 86, 20);
		panel_rightMotorData.add(tbox_rightMotorVoltage);

		JLabel lbl_rightMotorVoltage = new JLabel("Voltage (V):");
		lbl_rightMotorVoltage.setForeground(Color.WHITE);
		lbl_rightMotorVoltage.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorVoltage.setBounds(10, 126, 88, 14);
		panel_rightMotorData.add(lbl_rightMotorVoltage);

		JLabel lbl_rightMotorTemperature = new JLabel("Temp. (C):");
		lbl_rightMotorTemperature.setForeground(Color.WHITE);
		lbl_rightMotorTemperature.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorTemperature.setBounds(0, 101, 98, 14);
		panel_rightMotorData.add(lbl_rightMotorTemperature);

		tbox_rightMotorTemperature = new JTextField();
		tbox_rightMotorTemperature.setColumns(10);
		tbox_rightMotorTemperature.setBounds(108, 101, 86, 20);
		panel_rightMotorData.add(tbox_rightMotorTemperature);

		tbox_rightMotorMode = new JTextField();
		tbox_rightMotorMode.setColumns(10);
		tbox_rightMotorMode.setBounds(261, 126, 86, 20);
		panel_rightMotorData.add(tbox_rightMotorMode);

		JLabel lbl_rightMotorMode = new JLabel("Mode:");
		lbl_rightMotorMode.setForeground(Color.WHITE);
		lbl_rightMotorMode.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorMode.setBounds(179, 126, 72, 14);
		panel_rightMotorData.add(lbl_rightMotorMode);

		JLabel lbl_rightMotorSetpoint = new JLabel("Setpoint:");
		lbl_rightMotorSetpoint.setForeground(Color.WHITE);
		lbl_rightMotorSetpoint.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorSetpoint.setBounds(179, 101, 72, 14);
		panel_rightMotorData.add(lbl_rightMotorSetpoint);

		tbox_rightMotorSetpoint = new JTextField();
		tbox_rightMotorSetpoint.setColumns(10);
		tbox_rightMotorSetpoint.setBounds(261, 101, 86, 20);
		panel_rightMotorData.add(tbox_rightMotorSetpoint);

		tbox_rightMotorPosition = new JTextField();
		tbox_rightMotorPosition.setColumns(10);
		tbox_rightMotorPosition.setBounds(261, 73, 86, 20);
		panel_rightMotorData.add(tbox_rightMotorPosition);

		JLabel lbl_rightMotorPosition = new JLabel("Position:");
		lbl_rightMotorPosition.setForeground(Color.WHITE);
		lbl_rightMotorPosition.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorPosition.setBounds(179, 76, 72, 14);
		panel_rightMotorData.add(lbl_rightMotorPosition);

		JLabel lbl_rightMotorSpeed = new JLabel("Speed:");
		lbl_rightMotorSpeed.setForeground(Color.WHITE);
		lbl_rightMotorSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorSpeed.setBounds(179, 51, 72, 14);
		panel_rightMotorData.add(lbl_rightMotorSpeed);

		tbox_rightMotorSpeed = new JTextField();
		tbox_rightMotorSpeed.setColumns(10);
		tbox_rightMotorSpeed.setBounds(261, 48, 86, 20);
		panel_rightMotorData.add(tbox_rightMotorSpeed);

		JLabel lbl_rightMotorFLimit = new JLabel("F. Limit?:");
		lbl_rightMotorFLimit.setForeground(Color.WHITE);
		lbl_rightMotorFLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorFLimit.setBounds(10, 151, 88, 14);
		panel_rightMotorData.add(lbl_rightMotorFLimit);

		tbox_rightMotorFLimit = new JTextField();
		tbox_rightMotorFLimit.setColumns(10);
		tbox_rightMotorFLimit.setBounds(108, 151, 86, 20);
		panel_rightMotorData.add(tbox_rightMotorFLimit);

		JLabel lbl_rightMotorRLimit = new JLabel("R. Limit?:");
		lbl_rightMotorRLimit.setForeground(Color.WHITE);
		lbl_rightMotorRLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_rightMotorRLimit.setBounds(179, 151, 72, 14);
		panel_rightMotorData.add(lbl_rightMotorRLimit);

		tbox_rightMotorRLimit = new JTextField();
		tbox_rightMotorRLimit.setColumns(10);
		tbox_rightMotorRLimit.setBounds(261, 151, 86, 20);
		panel_rightMotorData.add(tbox_rightMotorRLimit);

		JLabel lbl_rightDriveMotor = new JLabel("Right Drive Motor");
		lbl_rightDriveMotor.setForeground(Color.WHITE);
		lbl_rightDriveMotor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lbl_rightDriveMotor.setBounds(10, 11, 337, 26);
		panel_rightMotorData.add(lbl_rightDriveMotor);

		JPanel panel_scoopMotorData = new JPanel();
		panel_scoopMotorData.setBackground(new Color(205, 133, 63));
		panel_scoopMotorData.setLayout(null);

		JLabel lbl_scoopMotorID = new JLabel("Device ID:");
		lbl_scoopMotorID.setForeground(Color.WHITE);
		lbl_scoopMotorID.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorID.setBounds(10, 51, 88, 14);
		panel_scoopMotorData.add(lbl_scoopMotorID);

		tbox_scoopMotorID = new JTextField();
		tbox_scoopMotorID.setColumns(10);
		tbox_scoopMotorID.setBounds(108, 48, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorID);

		JLabel lbl_scoopMotorCurrent = new JLabel("Current (A):");
		lbl_scoopMotorCurrent.setForeground(Color.WHITE);
		lbl_scoopMotorCurrent.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorCurrent.setBounds(10, 76, 88, 14);
		panel_scoopMotorData.add(lbl_scoopMotorCurrent);

		tbox_scoopMotorCurrent = new JTextField();
		tbox_scoopMotorCurrent.setColumns(10);
		tbox_scoopMotorCurrent.setBounds(108, 73, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorCurrent);

		tbox_scoopMotorVoltage = new JTextField();
		tbox_scoopMotorVoltage.setColumns(10);
		tbox_scoopMotorVoltage.setBounds(108, 126, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorVoltage);

		JLabel lbl_scoopMotorVoltage = new JLabel("Voltage (V):");
		lbl_scoopMotorVoltage.setForeground(Color.WHITE);
		lbl_scoopMotorVoltage.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorVoltage.setBounds(10, 126, 88, 14);
		panel_scoopMotorData.add(lbl_scoopMotorVoltage);

		JLabel lbl_scoopMotorTemperature = new JLabel("Temp. (C):");
		lbl_scoopMotorTemperature.setForeground(Color.WHITE);
		lbl_scoopMotorTemperature.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorTemperature.setBounds(-17, 101, 115, 14);
		panel_scoopMotorData.add(lbl_scoopMotorTemperature);

		tbox_scoopMotorTemperature = new JTextField();
		tbox_scoopMotorTemperature.setColumns(10);
		tbox_scoopMotorTemperature.setBounds(108, 101, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorTemperature);

		tbox_scoopMotorMode = new JTextField();
		tbox_scoopMotorMode.setColumns(10);
		tbox_scoopMotorMode.setBounds(261, 126, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorMode);

		JLabel lbl_scoopMotorMode = new JLabel("Mode:");
		lbl_scoopMotorMode.setForeground(Color.WHITE);
		lbl_scoopMotorMode.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorMode.setBounds(179, 126, 72, 14);
		panel_scoopMotorData.add(lbl_scoopMotorMode);

		JLabel lbl_scoopMotorSetpoint = new JLabel("Setpoint:");
		lbl_scoopMotorSetpoint.setForeground(Color.WHITE);
		lbl_scoopMotorSetpoint.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorSetpoint.setBounds(179, 101, 72, 14);
		panel_scoopMotorData.add(lbl_scoopMotorSetpoint);

		tbox_scoopMotorSetpoint = new JTextField();
		tbox_scoopMotorSetpoint.setColumns(10);
		tbox_scoopMotorSetpoint.setBounds(261, 101, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorSetpoint);

		tbox_scoopMotorPosition = new JTextField();
		tbox_scoopMotorPosition.setColumns(10);
		tbox_scoopMotorPosition.setBounds(261, 73, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorPosition);

		JLabel lbl_scoopMotorPosition = new JLabel("Position:");
		lbl_scoopMotorPosition.setForeground(Color.WHITE);
		lbl_scoopMotorPosition.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorPosition.setBounds(179, 76, 72, 14);
		panel_scoopMotorData.add(lbl_scoopMotorPosition);

		JLabel lbl_scoopMotorSpeed = new JLabel("Speed:");
		lbl_scoopMotorSpeed.setForeground(Color.WHITE);
		lbl_scoopMotorSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorSpeed.setBounds(179, 51, 72, 14);
		panel_scoopMotorData.add(lbl_scoopMotorSpeed);

		tbox_scoopMotorSpeed = new JTextField();
		tbox_scoopMotorSpeed.setColumns(10);
		tbox_scoopMotorSpeed.setBounds(261, 48, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorSpeed);

		JLabel lbl_scoopMotorFLimit = new JLabel("F. Limit?:");
		lbl_scoopMotorFLimit.setForeground(Color.WHITE);
		lbl_scoopMotorFLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorFLimit.setBounds(10, 151, 88, 14);
		panel_scoopMotorData.add(lbl_scoopMotorFLimit);

		tbox_scoopMotorFLimit = new JTextField();
		tbox_scoopMotorFLimit.setColumns(10);
		tbox_scoopMotorFLimit.setBounds(108, 151, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorFLimit);

		JLabel lbl_scoopMotorRLimit = new JLabel("R. Limit?:");
		lbl_scoopMotorRLimit.setForeground(Color.WHITE);
		lbl_scoopMotorRLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_scoopMotorRLimit.setBounds(179, 151, 72, 14);
		panel_scoopMotorData.add(lbl_scoopMotorRLimit);

		tbox_scoopMotorRLimit = new JTextField();
		tbox_scoopMotorRLimit.setColumns(10);
		tbox_scoopMotorRLimit.setBounds(261, 151, 86, 20);
		panel_scoopMotorData.add(tbox_scoopMotorRLimit);

		JLabel lbl_scoopMotor = new JLabel("Scoop Motor");
		lbl_scoopMotor.setForeground(Color.WHITE);
		lbl_scoopMotor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lbl_scoopMotor.setBounds(10, 11, 337, 26);
		panel_scoopMotorData.add(lbl_scoopMotor);

		JPanel panel_depthMotorData = new JPanel();
		panel_depthMotorData.setBackground(new Color(205, 133, 63));
		panel_depthMotorData.setLayout(null);

		JLabel lbl_depthMotorID = new JLabel("Device ID:");
		lbl_depthMotorID.setForeground(Color.WHITE);
		lbl_depthMotorID.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorID.setBounds(10, 51, 88, 14);
		panel_depthMotorData.add(lbl_depthMotorID);

		tbox_depthMotorID = new JTextField();
		tbox_depthMotorID.setColumns(10);
		tbox_depthMotorID.setBounds(108, 48, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorID);

		JLabel lbl_depthMotorCurrent = new JLabel("Current (A):");
		lbl_depthMotorCurrent.setForeground(Color.WHITE);
		lbl_depthMotorCurrent.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorCurrent.setBounds(10, 76, 88, 14);
		panel_depthMotorData.add(lbl_depthMotorCurrent);

		tbox_depthMotorCurrent = new JTextField();
		tbox_depthMotorCurrent.setColumns(10);
		tbox_depthMotorCurrent.setBounds(108, 73, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorCurrent);

		tbox_depthMotorVoltage = new JTextField();
		tbox_depthMotorVoltage.setColumns(10);
		tbox_depthMotorVoltage.setBounds(108, 126, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorVoltage);

		JLabel lbl_depthMotorVoltage = new JLabel("Voltage (V):");
		lbl_depthMotorVoltage.setForeground(Color.WHITE);
		lbl_depthMotorVoltage.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorVoltage.setBounds(10, 126, 88, 14);
		panel_depthMotorData.add(lbl_depthMotorVoltage);

		JLabel lbl_depthMotorTemperature = new JLabel("Temp. (C):");
		lbl_depthMotorTemperature.setForeground(Color.WHITE);
		lbl_depthMotorTemperature.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorTemperature.setBounds(0, 101, 98, 14);
		panel_depthMotorData.add(lbl_depthMotorTemperature);

		tbox_depthMotorTemperature = new JTextField();
		tbox_depthMotorTemperature.setColumns(10);
		tbox_depthMotorTemperature.setBounds(108, 101, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorTemperature);

		tbox_depthMotorMode = new JTextField();
		tbox_depthMotorMode.setColumns(10);
		tbox_depthMotorMode.setBounds(261, 126, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorMode);

		JLabel lbl_depthMotorMode = new JLabel("Mode:");
		lbl_depthMotorMode.setForeground(Color.WHITE);
		lbl_depthMotorMode.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorMode.setBounds(179, 126, 72, 14);
		panel_depthMotorData.add(lbl_depthMotorMode);

		JLabel lbl_depthMotorSetpoint = new JLabel("Setpoint:");
		lbl_depthMotorSetpoint.setForeground(Color.WHITE);
		lbl_depthMotorSetpoint.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorSetpoint.setBounds(179, 101, 72, 14);
		panel_depthMotorData.add(lbl_depthMotorSetpoint);

		tbox_depthMotorSetpoint = new JTextField();
		tbox_depthMotorSetpoint.setColumns(10);
		tbox_depthMotorSetpoint.setBounds(261, 101, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorSetpoint);

		tbox_depthMotorPosition = new JTextField();
		tbox_depthMotorPosition.setColumns(10);
		tbox_depthMotorPosition.setBounds(261, 73, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorPosition);

		JLabel lbl_depthMotorPosition = new JLabel("Position:");
		lbl_depthMotorPosition.setForeground(Color.WHITE);
		lbl_depthMotorPosition.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorPosition.setBounds(179, 76, 72, 14);
		panel_depthMotorData.add(lbl_depthMotorPosition);

		JLabel lbl_depthMotorSpeed = new JLabel("Speed:");
		lbl_depthMotorSpeed.setForeground(Color.WHITE);
		lbl_depthMotorSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorSpeed.setBounds(179, 51, 72, 14);
		panel_depthMotorData.add(lbl_depthMotorSpeed);

		tbox_depthMotorSpeed = new JTextField();
		tbox_depthMotorSpeed.setColumns(10);
		tbox_depthMotorSpeed.setBounds(261, 48, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorSpeed);

		JLabel lbl_depthMotorFLimit = new JLabel("F. Limit?:");
		lbl_depthMotorFLimit.setForeground(Color.WHITE);
		lbl_depthMotorFLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorFLimit.setBounds(10, 151, 88, 14);
		panel_depthMotorData.add(lbl_depthMotorFLimit);

		tbox_depthMotorFLimit = new JTextField();
		tbox_depthMotorFLimit.setColumns(10);
		tbox_depthMotorFLimit.setBounds(108, 151, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorFLimit);

		JLabel lbl_depthMotorRLimit = new JLabel("R. Limit?:");
		lbl_depthMotorRLimit.setForeground(Color.WHITE);
		lbl_depthMotorRLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_depthMotorRLimit.setBounds(179, 151, 72, 14);
		panel_depthMotorData.add(lbl_depthMotorRLimit);

		tbox_depthMotorRLimit = new JTextField();
		tbox_depthMotorRLimit.setColumns(10);
		tbox_depthMotorRLimit.setBounds(261, 151, 86, 20);
		panel_depthMotorData.add(tbox_depthMotorRLimit);

		JLabel lbl_depthMotor = new JLabel("Depth Motor");
		lbl_depthMotor.setForeground(Color.WHITE);
		lbl_depthMotor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lbl_depthMotor.setBounds(10, 11, 337, 26);
		panel_depthMotorData.add(lbl_depthMotor);

		JPanel panel_winchMotorData = new JPanel();
		panel_winchMotorData.setBackground(new Color(205, 133, 63));
		panel_winchMotorData.setLayout(null);

		JLabel lbl_winchMotorID = new JLabel("Device ID:");
		lbl_winchMotorID.setForeground(Color.WHITE);
		lbl_winchMotorID.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorID.setBounds(10, 51, 88, 14);
		panel_winchMotorData.add(lbl_winchMotorID);

		tbox_winchMotorID = new JTextField();
		tbox_winchMotorID.setColumns(10);
		tbox_winchMotorID.setBounds(108, 48, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorID);

		JLabel lbl_winchMotorCurrent = new JLabel("Current (A):");
		lbl_winchMotorCurrent.setForeground(Color.WHITE);
		lbl_winchMotorCurrent.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorCurrent.setBounds(10, 76, 88, 14);
		panel_winchMotorData.add(lbl_winchMotorCurrent);

		tbox_winchMotorCurrent = new JTextField();
		tbox_winchMotorCurrent.setColumns(10);
		tbox_winchMotorCurrent.setBounds(108, 73, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorCurrent);

		tbox_winchMotorVoltage = new JTextField();
		tbox_winchMotorVoltage.setColumns(10);
		tbox_winchMotorVoltage.setBounds(108, 126, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorVoltage);

		JLabel lbl_winchMotorVoltage = new JLabel("Voltage (V):");
		lbl_winchMotorVoltage.setForeground(Color.WHITE);
		lbl_winchMotorVoltage.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorVoltage.setBounds(10, 126, 88, 14);
		panel_winchMotorData.add(lbl_winchMotorVoltage);

		JLabel lbl_winchMotorTemperature = new JLabel("Temp. (C):");
		lbl_winchMotorTemperature.setForeground(Color.WHITE);
		lbl_winchMotorTemperature.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorTemperature.setBounds(0, 101, 98, 14);
		panel_winchMotorData.add(lbl_winchMotorTemperature);

		tbox_winchMotorTemperature = new JTextField();
		tbox_winchMotorTemperature.setColumns(10);
		tbox_winchMotorTemperature.setBounds(108, 101, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorTemperature);

		tbox_winchMotorMode = new JTextField();
		tbox_winchMotorMode.setColumns(10);
		tbox_winchMotorMode.setBounds(261, 126, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorMode);

		JLabel lbl_winchMotorMode = new JLabel("Mode:");
		lbl_winchMotorMode.setForeground(Color.WHITE);
		lbl_winchMotorMode.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorMode.setBounds(179, 126, 72, 14);
		panel_winchMotorData.add(lbl_winchMotorMode);

		JLabel lbl_winchMotorSetpoint = new JLabel("Setpoint:");
		lbl_winchMotorSetpoint.setForeground(Color.WHITE);
		lbl_winchMotorSetpoint.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorSetpoint.setBounds(179, 101, 72, 14);
		panel_winchMotorData.add(lbl_winchMotorSetpoint);

		tbox_winchMotorSetpoint = new JTextField();
		tbox_winchMotorSetpoint.setColumns(10);
		tbox_winchMotorSetpoint.setBounds(261, 101, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorSetpoint);

		tbox_winchMotorPosition = new JTextField();
		tbox_winchMotorPosition.setColumns(10);
		tbox_winchMotorPosition.setBounds(261, 73, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorPosition);

		JLabel lbl_winchMotorPosition = new JLabel("Position:");
		lbl_winchMotorPosition.setForeground(Color.WHITE);
		lbl_winchMotorPosition.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorPosition.setBounds(179, 76, 72, 14);
		panel_winchMotorData.add(lbl_winchMotorPosition);

		JLabel lbl_winchMotorSpeed = new JLabel("Speed:");
		lbl_winchMotorSpeed.setForeground(Color.WHITE);
		lbl_winchMotorSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorSpeed.setBounds(179, 51, 72, 14);
		panel_winchMotorData.add(lbl_winchMotorSpeed);

		tbox_winchMotorSpeed = new JTextField();
		tbox_winchMotorSpeed.setColumns(10);
		tbox_winchMotorSpeed.setBounds(261, 48, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorSpeed);

		JLabel lbl_winchMotorFLimit = new JLabel("F. Limit?:");
		lbl_winchMotorFLimit.setForeground(Color.WHITE);
		lbl_winchMotorFLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorFLimit.setBounds(10, 151, 88, 14);
		panel_winchMotorData.add(lbl_winchMotorFLimit);

		tbox_winchMotorFLimit = new JTextField();
		tbox_winchMotorFLimit.setColumns(10);
		tbox_winchMotorFLimit.setBounds(108, 151, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorFLimit);

		JLabel lbl_winchMotorRLimit = new JLabel("R. Limit?:");
		lbl_winchMotorRLimit.setForeground(Color.WHITE);
		lbl_winchMotorRLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_winchMotorRLimit.setBounds(179, 151, 72, 14);
		panel_winchMotorData.add(lbl_winchMotorRLimit);

		tbox_winchMotorRLimit = new JTextField();
		tbox_winchMotorRLimit.setColumns(10);
		tbox_winchMotorRLimit.setBounds(261, 151, 86, 20);
		panel_winchMotorData.add(tbox_winchMotorRLimit);

		JLabel lbl_winchMotor = new JLabel("Dump Motor");
		lbl_winchMotor.setForeground(Color.WHITE);
		lbl_winchMotor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lbl_winchMotor.setBounds(10, 11, 337, 26);
		panel_winchMotorData.add(lbl_winchMotor);

		JPanel panel_SensorData = new JPanel();
		panel_SensorData.setLayout(null);


		/*************************************************************************/
		JLabel lbl_imu = new JLabel("IMU:");
		lbl_imu.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_imu.setBounds(400, 11, 337, 26);
		panel_SensorData.add(lbl_imu);

//		tbox_sensorData = new JTextField();
//		tbox_sensorData.setColumns(10);
//		tbox_sensorData.setBounds(108, 48, 86, 20);
//		panel_depthMotorData.add(tbox_sensorData);

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(70, 130, 180));
		panel_4.setLayout(null);

		JLabel lblImuReadings = new JLabel("IMU Readings:");
		lblImuReadings.setForeground(Color.WHITE);
		lblImuReadings.setHorizontalAlignment(SwingConstants.TRAILING);
		lblImuReadings.setBounds(10, 51, 100, 14);
		panel_4.add(lblImuReadings);

		tbox_imuData = new JTextField();
		tbox_imuData.setColumns(10);
		tbox_imuData.setBounds(116, 48, 86, 20);
		panel_4.add(tbox_imuData);

		JLabel lblCameraAngle1 = new JLabel("Camera Angle:");
		lblCameraAngle1.setForeground(Color.WHITE);
		lblCameraAngle1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCameraAngle1.setBounds(10, 78, 100, 14);
		panel_4.add(lblCameraAngle1);

		tbox_cameraAngleData1 = new JTextField();
		tbox_cameraAngleData1.setColumns(10);
		tbox_cameraAngleData1.setBounds(116, 75, 86, 20);
		panel_4.add(tbox_cameraAngleData1);

		JLabel lblSensordata = new JLabel("Sensor Data");
		lblSensordata.setForeground(Color.WHITE);
		lblSensordata.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblSensordata.setBounds(10, 11, 337, 26);
		panel_4.add(lblSensordata);

		GroupLayout gl_panel_robotData = new GroupLayout(panel_robotData);
		gl_panel_robotData.setHorizontalGroup(
				gl_panel_robotData.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_robotData.createSequentialGroup()
								.addGap(26)
								.addGroup(gl_panel_robotData.createParallelGroup(Alignment.LEADING)
										.addComponent(panel_leftMotorData, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
										.addComponent(panel_scoopMotorData, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
										.addComponent(panel_winchMotorData, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_panel_robotData.createParallelGroup(Alignment.LEADING)
										.addComponent(panel_rightMotorData, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
										.addComponent(panel_depthMotorData, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
										.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(26, Short.MAX_VALUE))
		);
		gl_panel_robotData.setVerticalGroup(
				gl_panel_robotData.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_robotData.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel_robotData.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_panel_robotData.createSequentialGroup()
												.addComponent(panel_leftMotorData, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(panel_scoopMotorData, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(panel_winchMotorData, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_panel_robotData.createSequentialGroup()
												.addComponent(panel_rightMotorData, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(panel_depthMotorData, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)))
								.addContainerGap())
		);
		
		JLabel lblCameraAngle2 = new JLabel("Robot Pose:");
		lblCameraAngle2.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCameraAngle2.setForeground(Color.WHITE);
		lblCameraAngle2.setBounds(10, 105, 100, 14);
		panel_4.add(lblCameraAngle2);
		
		tbox_cameraAngleData2 = new JTextField();
		tbox_cameraAngleData2.setColumns(10);
		tbox_cameraAngleData2.setBounds(116, 102, 86, 20);
		panel_4.add(tbox_cameraAngleData2);
		
		JLabel lblIR = new JLabel("Which Camera?: ");
		lblIR.setHorizontalAlignment(SwingConstants.TRAILING);
		lblIR.setForeground(Color.WHITE);
		lblIR.setBounds(-6, 133, 120, 14);
		panel_4.add(lblIR);
		
		tbox_IRData = new JTextField();
		tbox_IRData.setColumns(10);
		tbox_IRData.setBounds(116, 130, 86, 20);
		panel_4.add(tbox_IRData);
		
		lblBumpLeft = new JLabel("LEFT");
		lblBumpLeft.setBackground(Color.GRAY);
		lblBumpLeft.setOpaque(true);
		lblBumpLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblBumpLeft.setForeground(Color.WHITE);
		lblBumpLeft.setBounds(220, 74, 58, 45);
		panel_4.add(lblBumpLeft);
		
		lblBumpRight = new JLabel("RIGHT");
		lblBumpRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblBumpRight.setForeground(Color.WHITE);
		lblBumpRight.setBackground(Color.GRAY);
		lblBumpRight.setOpaque(true);
		lblBumpRight.setBounds(289, 74, 58, 45);
		panel_4.add(lblBumpRight);
		
		JLabel lblBumps = new JLabel("Bump Sensors");
		lblBumps.setFont(new Font("Dialog", Font.BOLD, 14));
		lblBumps.setHorizontalAlignment(SwingConstants.CENTER);
		lblBumps.setForeground(Color.WHITE);
		lblBumps.setBounds(220, 50, 127, 14);
		panel_4.add(lblBumps);
		panel_leftMotorData.setLayout(null);


		JLabel lbl_leftMotorID = new JLabel("Device ID:");
		lbl_leftMotorID.setForeground(Color.WHITE);
		lbl_leftMotorID.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_leftMotorID.setBounds(10, 51, 88, 14);
		panel_leftMotorData.add(lbl_leftMotorID);

		tbox_leftMotorID = new JTextField();
		tbox_leftMotorID.setBounds(108, 48, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorID);
		tbox_leftMotorID.setColumns(10);

		JLabel lbl_leftMotorCurrent = new JLabel("Current (A):");
		lbl_leftMotorCurrent.setForeground(Color.WHITE);
		lbl_leftMotorCurrent.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_leftMotorCurrent.setBounds(10, 76, 88, 14);
		panel_leftMotorData.add(lbl_leftMotorCurrent);

		tbox_leftMotorCurrent = new JTextField();
		tbox_leftMotorCurrent.setColumns(10);
		tbox_leftMotorCurrent.setBounds(108, 73, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorCurrent);

		tbox_leftMotorVoltage = new JTextField();
		tbox_leftMotorVoltage.setColumns(10);
		tbox_leftMotorVoltage.setBounds(108, 126, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorVoltage);

		JLabel leftMotorVoltage = new JLabel("Voltage (V):");
		leftMotorVoltage.setForeground(Color.WHITE);
		leftMotorVoltage.setHorizontalAlignment(SwingConstants.TRAILING);
		leftMotorVoltage.setBounds(10, 126, 88, 14);
		panel_leftMotorData.add(leftMotorVoltage);

		JLabel lbl_leftMotorTemperature = new JLabel("Temp. (C):");
		lbl_leftMotorTemperature.setForeground(Color.WHITE);
		lbl_leftMotorTemperature.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_leftMotorTemperature.setBounds(-11, 101, 109, 14);
		panel_leftMotorData.add(lbl_leftMotorTemperature);

		tbox_leftMotorTemperature = new JTextField();
		tbox_leftMotorTemperature.setColumns(10);
		tbox_leftMotorTemperature.setBounds(108, 101, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorTemperature);

		tbox_leftMotorMode = new JTextField();
		tbox_leftMotorMode.setColumns(10);
		tbox_leftMotorMode.setBounds(261, 126, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorMode);

		JLabel lbl_leftMotorMode = new JLabel("Mode:");
		lbl_leftMotorMode.setForeground(Color.WHITE);
		lbl_leftMotorMode.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_leftMotorMode.setBounds(179, 126, 72, 14);
		panel_leftMotorData.add(lbl_leftMotorMode);

		JLabel lbl_leftMotorSetpoint = new JLabel("Setpoint:");
		lbl_leftMotorSetpoint.setForeground(Color.WHITE);
		lbl_leftMotorSetpoint.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_leftMotorSetpoint.setBounds(179, 101, 72, 14);
		panel_leftMotorData.add(lbl_leftMotorSetpoint);

		tbox_leftMotorSetpoint = new JTextField();
		tbox_leftMotorSetpoint.setColumns(10);
		tbox_leftMotorSetpoint.setBounds(261, 101, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorSetpoint);

		tbox_leftMotorPosition = new JTextField();
		tbox_leftMotorPosition.setColumns(10);
		tbox_leftMotorPosition.setBounds(261, 73, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorPosition);

		JLabel lbl_leftMotorPosition = new JLabel("Position:");
		lbl_leftMotorPosition.setForeground(Color.WHITE);
		lbl_leftMotorPosition.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_leftMotorPosition.setBounds(179, 76, 72, 14);
		panel_leftMotorData.add(lbl_leftMotorPosition);

		JLabel lbl_leftMotorSpeed = new JLabel("Speed:");
		lbl_leftMotorSpeed.setForeground(Color.WHITE);
		lbl_leftMotorSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_leftMotorSpeed.setBounds(179, 51, 72, 14);
		panel_leftMotorData.add(lbl_leftMotorSpeed);

		tbox_leftMotorSpeed = new JTextField();
		tbox_leftMotorSpeed.setColumns(10);
		tbox_leftMotorSpeed.setBounds(261, 48, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorSpeed);

		JLabel lbl_leftMotorFLimit = new JLabel("F. Limit?:");
		lbl_leftMotorFLimit.setForeground(Color.WHITE);
		lbl_leftMotorFLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_leftMotorFLimit.setBounds(10, 151, 88, 14);
		panel_leftMotorData.add(lbl_leftMotorFLimit);

		tbox_leftMotorFLimit = new JTextField();
		tbox_leftMotorFLimit.setColumns(10);
		tbox_leftMotorFLimit.setBounds(108, 151, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorFLimit);

		JLabel lbl_leftMotorRLimit = new JLabel("R. Limit?:");
		lbl_leftMotorRLimit.setForeground(Color.WHITE);
		lbl_leftMotorRLimit.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_leftMotorRLimit.setBounds(179, 151, 72, 14);
		panel_leftMotorData.add(lbl_leftMotorRLimit);

		tbox_leftMotorRLimit = new JTextField();
		tbox_leftMotorRLimit.setColumns(10);
		tbox_leftMotorRLimit.setBounds(261, 151, 86, 20);
		panel_leftMotorData.add(tbox_leftMotorRLimit);

		JLabel lbl_leftMotor = new JLabel("Left Drive Motor");
		lbl_leftMotor.setForeground(Color.WHITE);
		lbl_leftMotor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lbl_leftMotor.setBounds(10, 11, 337, 26);
		panel_leftMotorData.add(lbl_leftMotor);
		panel_robotData.setLayout(gl_panel_robotData);

		getContentPane().setLayout(groupLayout);

	}

	public static void updateMessageQueueList(JList<String> messageList) {
		model.clear();
		for (int i = 0; i < messageQueue.getSize(); i++) {
			Message msg = messageQueue.peekAtIndex(i);
			String listItem = "";
			listItem += "(" + i + ") ";
			listItem += msg.getType().toString() + ": ";
			listItem += msg.getMessageString();
			model.addElement(listItem);
		}
	}

	public static void updateRecoveryStackList(JList<String> list_1){
		recovery_model.clear();
		for (int i = 0; i<recoveryStack.getSize();i++){
			Message msg = recoveryStack.peekAtIndex(i);
//			System.out.println(msg.getSize());
			String listItem = "";
			listItem += "(" + i + ") ";
			listItem += msg.getType().toString() + ": ";
			listItem += msg.convertRecoveryMessageString(); //This is where the opposite function is created
//			recovery_model.addElement(listItem);
			recovery_model.add(0, listItem);
		}
	}

	private void setCommandComboBoxStrings(JComboBox<String> cbox) {
		// these need to be in the same order as MessageType
		cbox.addItem("STOP");
		cbox.addItem("Drive Time");
		cbox.addItem("Drive Distance");
		cbox.addItem("Rotate Time");
		cbox.addItem("Scoop Time");
		cbox.addItem("Scoop Distance");
		cbox.addItem("Depth Time");
		cbox.addItem("Depth Distance");
		cbox.addItem("Rotate-->Center");
		cbox.addItem("Bucket Distance");
		cbox.addItem("Get Target");
		cbox.addItem("Stop Time");
		cbox.addItem("Motor Values");
		cbox.addItem("Rotate --> Perpendicular");
		cbox.addItem("Ping Test");
		cbox.addItem("Dig Time");
		cbox.addItem("Retract Digger");
		cbox.addItem("Go to Dump");
		cbox.addItem("Dump");
	}

	private void updateMessageLabels() {
		for (int i = 0; i < 8; i++) {
			System.out.println(selectedMessage.getDataTagByIndex(i));
			if (selectedMessage != null) {
				messageLabels[i].setText(selectedMessage.getDataTagByIndex(i));
			}
		}
	}

	/**
	 * Updates the robot data boxes in the GUI
	 * <p>
	 * Convention Followed-
	 * Blue = +
//	 * Orange = -
//	 * RED = Extreme Vals
	 */
	private static void updateRobotDataBoxes() {

		if (robotData.getHb().getValue().intValue()==1){hbLabel.setBackground(new Color(255, 0, 255));}
		else{
			hbLabel.setBackground(Color.GRAY);
		}

//		TODO: Test recovery for the motors
//		*******LEFT MOTOR******
		tbox_leftMotorID.setText((robotData.getLeftMotor().getDeviceID().toString()));
		tbox_leftMotorCurrent.setText((robotData.getLeftMotor().getCurrent().toString()));
		if ((robotData.getLeftMotor().getCurrent().floatValue()) >= 20.0) {runRecovery(); tbox_leftMotorCurrent.setBackground(Color.GRAY);}
		else if ((robotData.getLeftMotor().getCurrent().floatValue())< 20.0) {tbox_leftMotorCurrent.setBackground(Color.WHITE);}
		tbox_leftMotorVoltage.setText((robotData.getLeftMotor().getVoltage().toString()));
		tbox_leftMotorTemperature.setText((robotData.getLeftMotor().getTemperature().toString()));
		tbox_leftMotorMode.setText((robotData.getLeftMotor().getMode().toString()));

		tbox_leftMotorSetpoint.setText((robotData.getLeftMotor().getSetpoint().toString()));
		if (robotData.getLeftMotor().getSetpoint()>0.0){tbox_leftMotorSetpoint.setBackground(Color.BLUE);}
		else if(robotData.getLeftMotor().getSetpoint()<0.0){tbox_leftMotorSetpoint.setBackground(Color.ORANGE);}
		else if (robotData.getLeftMotor().getSetpoint() == 0.0){tbox_leftMotorSetpoint.setBackground(Color.WHITE);}

		tbox_leftMotorPosition.setText((robotData.getLeftMotor().getPosition().toString()));
		if (abs(robotData.getLeftMotor().getPosition())>10){tbox_leftMotorPosition.setBackground(Color.RED);}
		else if (robotData.getLeftMotor().getPosition() == 0){tbox_leftMotorPosition.setBackground(Color.WHITE);}

		tbox_leftMotorSpeed.setText((robotData.getLeftMotor().getSpeed().toString()));
		tbox_leftMotorFLimit.setText((robotData.getLeftMotor().getForwardLimit().toString()));
		tbox_leftMotorRLimit.setText((robotData.getLeftMotor().getReverseLimit().toString()));

//	 	************RIGHT MOTOR**********
		tbox_rightMotorID.setText((robotData.getRightMotor().getDeviceID().toString()));
		tbox_rightMotorCurrent.setText((robotData.getRightMotor().getCurrent().toString()));

		if((robotData.getRightMotor().getCurrent().floatValue() >= 20.0)){runRecovery(); tbox_rightMotorCurrent.setBackground(Color.GRAY);}
		else if((robotData.getRightMotor().getCurrent().floatValue() < 20.0)){tbox_rightMotorCurrent.setBackground(Color.WHITE);}


		tbox_rightMotorVoltage.setText((robotData.getRightMotor().getVoltage().toString()));
		tbox_rightMotorTemperature.setText((robotData.getRightMotor().getTemperature().toString()));
		tbox_rightMotorMode.setText((robotData.getRightMotor().getMode().toString()));
		tbox_rightMotorSetpoint.setText((robotData.getRightMotor().getSetpoint().toString()));
		if (robotData.getRightMotor().getSetpoint()>0.0){tbox_rightMotorSetpoint.setBackground(Color.BLUE);}
		else if(robotData.getRightMotor().getSetpoint()<0.0){tbox_rightMotorSetpoint.setBackground(Color.ORANGE);}
		else if (robotData.getRightMotor().getSetpoint() == 0.0){tbox_rightMotorSetpoint.setBackground(Color.WHITE);}

		tbox_rightMotorPosition.setText((robotData.getRightMotor().getPosition().toString()));
		if (abs(robotData.getRightMotor().getPosition())>10) {tbox_rightMotorPosition.setBackground(Color.RED);}
		else if (robotData.getRightMotor().getPosition() == 0){tbox_rightMotorPosition.setBackground(Color.WHITE);}


		tbox_rightMotorSpeed.setText((robotData.getRightMotor().getSpeed().toString()));
		tbox_rightMotorFLimit.setText((robotData.getRightMotor().getForwardLimit().toString()));
		tbox_rightMotorRLimit.setText((robotData.getRightMotor().getReverseLimit().toString()));


//******SCOOP MOTOR***************
		tbox_scoopMotorID.setText((robotData.getScoopMotor().getDeviceID().toString()));
		tbox_scoopMotorCurrent.setText((robotData.getScoopMotor().getCurrent().toString()));
	// Recovery for scoop action
		if (robotData.getScoopMotor().getCurrent() >= 20.0){runRecovery(); tbox_scoopMotorCurrent.setBackground(Color.GRAY);}
		else if (robotData.getScoopMotor().getCurrent()<20.0) {tbox_scoopMotorCurrent.setBackground(Color.WHITE);}
		tbox_scoopMotorVoltage.setText((robotData.getScoopMotor().getVoltage().toString()));
		tbox_scoopMotorTemperature.setText((robotData.getScoopMotor().getTemperature().toString()));
		tbox_scoopMotorMode.setText((robotData.getScoopMotor().getMode().toString()));
		tbox_scoopMotorSetpoint.setText((robotData.getScoopMotor().getSetpoint().toString()));
		if (robotData.getScoopMotor().getSetpoint()>0.0){tbox_scoopMotorSetpoint.setBackground(Color.BLUE);}
		else if(robotData.getScoopMotor().getSetpoint()<0.0){tbox_scoopMotorSetpoint.setBackground(Color.ORANGE);}
		else if (robotData.getScoopMotor().getSetpoint() == 0.0){tbox_scoopMotorSetpoint.setBackground(Color.WHITE);}


		tbox_scoopMotorPosition.setText((robotData.getScoopMotor().getPosition().toString()));
		if (abs(robotData.getScoopMotor().getPosition())>10) {tbox_scoopMotorPosition.setBackground(Color.RED);}
		else if (robotData.getScoopMotor().getPosition() == 0){tbox_scoopMotorPosition.setBackground(Color.WHITE);}

		tbox_scoopMotorSpeed.setText((robotData.getScoopMotor().getSpeed().toString()));
		tbox_scoopMotorFLimit.setText((robotData.getScoopMotor().getForwardLimit().toString()));
		tbox_scoopMotorRLimit.setText((robotData.getScoopMotor().getReverseLimit().toString()));

//		*************DEPTH MOTOR************
		tbox_depthMotorID.setText((robotData.getDepthMotor().getDeviceID().toString()));
		tbox_depthMotorCurrent.setText((robotData.getDepthMotor().getCurrent().toString()));
//		Recovery for the depth motor (aka MSG_DIG most of the time)
		if (robotData.getDepthMotor().getCurrent() >= 25.0){ runRecovery(); tbox_depthMotorCurrent.setBackground(Color.GRAY);}
		else if (robotData.getDepthMotor().getCurrent() < 25.0){tbox_depthMotorCurrent.setBackground(Color.WHITE);}
		tbox_depthMotorVoltage.setText((robotData.getDepthMotor().getVoltage().toString()));
		tbox_depthMotorTemperature.setText((robotData.getDepthMotor().getTemperature().toString()));
		tbox_depthMotorMode.setText((robotData.getDepthMotor().getMode().toString()));
		tbox_depthMotorSetpoint.setText((robotData.getDepthMotor().getSetpoint().toString()));
		if ((robotData.getDepthMotor().getSetpoint())>0.3) {tbox_depthMotorSetpoint.setBackground(Color.RED);}
		else if (robotData.getDepthMotor().getSetpoint()>0.0){tbox_depthMotorSetpoint.setBackground(Color.BLUE);}
		else if(robotData.getDepthMotor().getSetpoint()<0.0){tbox_depthMotorSetpoint.setBackground(Color.ORANGE);}
		else if (robotData.getDepthMotor().getSetpoint() == 0.0){tbox_depthMotorSetpoint.setBackground(Color.WHITE);}



		tbox_depthMotorPosition.setText((robotData.getDepthMotor().getPosition().toString()));
		if (abs(robotData.getDepthMotor().getPosition())>45) {tbox_depthMotorPosition.setBackground(Color.ORANGE);}
		else if (robotData.getDepthMotor().getPosition() > 0){tbox_depthMotorPosition.setBackground(Color.GREEN);}
		else if (robotData.getDepthMotor().getPosition() == 0){tbox_depthMotorPosition.setBackground(Color.WHITE);}

		tbox_depthMotorSpeed.setText((robotData.getDepthMotor().getSpeed().toString()));
		tbox_depthMotorFLimit.setText((robotData.getDepthMotor().getForwardLimit().toString()));
		tbox_depthMotorRLimit.setText((robotData.getDepthMotor().getReverseLimit().toString()));

//		***********Dump Motor*************
		tbox_winchMotorID.setText((robotData.getDumpMotor().getDeviceID().toString()));
		tbox_winchMotorCurrent.setText((robotData.getDumpMotor().getCurrent().toString()));
		if (robotData.getDumpMotor().getCurrent() >= 15.0){ runRecovery(); tbox_winchMotorCurrent.setBackground(Color.GRAY);}
		if (robotData.getDumpMotor().getCurrent() < 15.0) { tbox_winchMotorCurrent.setBackground(Color.WHITE);}

		tbox_winchMotorVoltage.setText((robotData.getDumpMotor().getVoltage().toString()));
		tbox_winchMotorTemperature.setText((robotData.getDumpMotor().getTemperature().toString()));
		tbox_winchMotorMode.setText((robotData.getDumpMotor().getMode().toString()));
		tbox_winchMotorSetpoint.setText((robotData.getDumpMotor().getSetpoint().toString()));
		if (robotData.getDumpMotor().getSetpoint()>0.0){tbox_winchMotorSetpoint.setBackground(Color.BLUE);}
		else if(robotData.getDumpMotor().getSetpoint()<0.0){tbox_winchMotorSetpoint.setBackground(Color.ORANGE);}
		else if (robotData.getDumpMotor().getSetpoint() == 0.0){tbox_winchMotorSetpoint.setBackground(Color.WHITE);}


		tbox_winchMotorPosition.setText((robotData.getDumpMotor().getPosition().toString()));
		if (abs(robotData.getDumpMotor().getPosition())> 20) {tbox_winchMotorPosition.setBackground(Color.ORANGE);}
		else if (robotData.getDumpMotor().getPosition() > 0){tbox_winchMotorPosition.setBackground(Color.GREEN);}
		else if (robotData.getDumpMotor().getPosition() == 0){tbox_winchMotorPosition.setBackground(Color.WHITE);}

		tbox_winchMotorSpeed.setText((robotData.getDumpMotor().getSpeed().toString()));
		tbox_winchMotorFLimit.setText((robotData.getDumpMotor().getForwardLimit().toString()));
		tbox_winchMotorRLimit.setText((robotData.getDumpMotor().getReverseLimit().toString()));

	//********Sensors*****************
		tbox_imuData.setText((robotData.getImu().getValue().toString()));
		tbox_cameraAngleData1.setText((robotData.getCamServo1().getValue().toString()));
		tbox_cameraAngleData2.setText((robotData.getPose().getValue().toString()));
//		TODO: add the lblBumpRight button display
//		System.out.print("Left = ");
//		System.out.println(robotData.getLeftBump().getValue());
//		System.out.print("Right = ");
//		System.out.println(robotData.getRightBump().getValue());

		if (robotData.getLeftBump().getValue().floatValue() < 1.0){lblBumpLeft.setBackground(Color.GREEN);}
		else if (robotData.getLeftBump().getValue().floatValue() == 1.0){lblBumpLeft.setBackground(Color.GRAY);}
//		Bump Right Display
		if (robotData.getRightBump().getValue().floatValue() < 1.0){lblBumpRight.setBackground(Color.GREEN);}
		else if (robotData.getRightBump().getValue().floatValue() == 1.0){lblBumpRight.setBackground(Color.GRAY);}

		pose = robotData.getPose().getValue();

		if (robotData.getCamera().getValue()==1.0){ tbox_IRData.setText("Right");}
		else if (robotData.getCamera().getValue()==2.0){ tbox_IRData.setText("Left");}
		else if(robotData.getCamera().getValue()!= 1.0 || robotData.getCamera().getValue()!= 2.0) { tbox_IRData.setText("Janeen");}

	}

	private static void runRecovery() {
		System.out.println((char)27 + "[31m" + "###################### !!RAN RECOVERY!! ########################");
		if (!recoveryStack.isEmpty()){
//			if already_ran_recovery_message !=
			already_ran_recovery_message = recoveryStack.peek();
			Message recovery_method = recoveryStack.pop();
			messageQueue.addAtFront(recovery_method);
			updateMessageQueueList(messageList);
			updateRecoveryStackList(list_1);
		}
	}

	public GUI(MessageQueue messageQueue) {
		getContentPane().setBackground(Color.DARK_GRAY);
		initialize(messageQueue);
	}

	//	private void updateDataBoxes()
	public static void updateAutonomyQueue(MessageQueue q){
		MessageType stopMessageType = MessageType.MSG_STOP;
		Message stopMessage = new MsgStop();

//		Message driveDist = new MsgDriveDistance();
//		driveDist.setDataByIndex(0,5.0);//Setting Distance
//		driveDist.setDataByIndex(1,0.6); //Distance, Speed

		Message driveTime = new MsgDriveTime();
		driveTime.setDataByIndex(0,5.0);//Setting Time
		driveTime.setDataByIndex(1,0.6); //Setting Speed

		Message driveTimeBack = new MsgDriveTime();
		driveTimeBack.setDataByIndex(0,5.0);//Setting Time
		driveTimeBack.setDataByIndex(1,-0.6); //Setting Speed

		Message findTarget = new MsgGetTarget();
		Message alignWithBorder = new MsgRotateCenter();

		Message driveToBorder = new MsgDriveTime(4.0, 1.0);
		Message rotateToStraight = new MsgRotatePerpendicular();

//		double camServoData = robotData.getCamServo1().getValue();
//		System.out.println("Can set camera Servo Data in the queue");
//		System.out.println(camServoData);


		/**********Stage 1**************/
//		q.addAtBack(stopMessage);
//		q.addAtBack(findTarget);
//		q.addAtBack(alignWithBorder);
//		q.addAtBack(driveToBorder);
//		q.addAtBack(rotateToStraight);
//		q.addAtBack(stopMessage);
		/*******************************/
		Message testdrive1 = new MsgDriveTime(2.0, -1.0);
		Message testdrive2 = new MsgDriveTime(2.0, -1.0);
		Message testdrive3 = new MsgDriveTime(2.0, -1.0);
		Message rotate = new MsgRotateTime();

//		q.addAtBack(testdrive1);
//		q.addAtBack(stopMessage);
//		q.addAtBack(testdrive2);
//		q.addAtBack(rotate);
//		q.addAtBack(stopMessage);
//		q.addAtBack(testdrive3);


//		updateMessageQueueList(messageList);
//		selectedMessage = MessageFactory.makeMessage(selectedMessageType);

		/************Stage 2***************/
// TODO: Change the parameters accordingly once tested
		Message drive_to_ez = new MsgDriveTime(2.0, 1.0); // TODO: Change the parameters accordingly
		Message excavate = new MsgDigTime(5.0);
		Message retract_digger = new MsgRetractDigger();
		Message go_back = new MsgGoToDump();
		Message dump = new MsgDump();

//		q.addAtBack(drive_to_ez);
//		q.addAtBack(excavate);
//		q.addAtBack(retract_digger);
		q.addAtBack(go_back);
//		q.addAtBack(dump);
		q.addAtBack(stopMessage);

	}

	public static void updatePingTestQueue(MessageQueue pq){
//		Message test_drive_motor = new MsgDriveTime(0.1, 0.2);
//		System.out.print("Left pos=");
		Message ping_test = new MsgPingTest(1.0);
//		System.out.println("Left Pos:" + robotData.getLeftMotor().getPosition().intValue());
//		Message test_drive_motor_finish = new MsgRotateTime(0.1, -0.2);
		Message stop = new MsgStop();
		pq.addAtBack(ping_test);
//		pq.addAtBack(stop);
//		pq.addAtBack(test_drive_motor_finish);
	}
}
