package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import data.EndPlayingListener;
import data.NikeDataGetter;
import data.NikeDataListener;
import data.NikeDataLoader;
import data.NikeDataPlayer;
import data.NikeDataRecorder;
import data.NikeRecordInfo;
import data.PlayTimeListener;
import data.RecordDataGetter;
import gnu.io.CommPortIdentifier;
import sensor.Direction;
import sensor.NikeSensor;
import sensor.NikeSensorConnector;
import sensor.NikeSensorResource;
import sensor.SensorPoints;
import sensor.ShoeBaseSizeLoader;

/**
 * ��App���D�n�����A�k�䬰���O�P�����Y�ɼ����e���P�����\��ﶵ�A���䬰�]�w�P�Y�ɷP�����ƭȡC
 * 
 * @author mingjia
 */

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 867266188462008355L;
	
	/**
	 * �]�wCommPort�P�c��Size��Panel
	 */
	private SettingsPanel settingsPanel;
	
	/**
	 * ���c�ԷP�����ƭȪ�Panel
	 */
	private NikeSensorPanel leftSensorPanel;
	
	/**
	 * �k�c�ԷP�����ƭȪ�Panel
	 */
	private NikeSensorPanel rightSensorPanel;
	
	/**
	 * �}�l�O�������s
	 */
	private JButton startButton;
	
	/**
	 * ����O�������s
	 */
	private JButton stopButton;
	
	/**
	 * �}�ҰO���ɦs���m��檺���s
	 */
	private JButton recordDirButton;
	
	/**
	 * ��ܥثe�O���ɦs���m����r���A�w�]�����i��ʧ���r
	 */
	private JTextField recordDirText;
	
	/**
	 * ����W�٪���r���
	 */
	private JTextField expNameText;
	
	/**
	 * ���ժ̪���r���
	 */
	private JTextField testerNameText;
	
	/**
	 * ���}���O�P�����ƭȼ�����Panel
	 */
	private MonitorPanel leftMonitorPanel;
	
	/**
	 * �k�}���O�P�����ƭȼ�����Panel
	 */
	private MonitorPanel rightMonitorPanel;
	
	/**
	 * �O�����񾹿ﶵ��Panel
	 */
	private PlayerPanel playerPanel;
	
	/**
	 * �ƭȰO����
	 */
	private NikeDataRecorder dataRecorder;
	
	/**
	 * �c�Ԥj�p���Ū����
	 */
	private ShoeBaseSizeLoader shoeBaseSizeLoader;
	
	/**
	 * �إߥD�����A�i�]�wtitle���ܥ����������D
	 * 
	 * @param title
	 */
	public MainFrame(String title) {
		super(title);
		
		//��l�ưʧ@�|�i���ƪ�Ū���A�Y���Ū���ɵo�Ϳ��~�N�|���ͨҥ~�A�õ����{���C
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//�D������Panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		GridBagLayout mainLayout = new GridBagLayout();
		mainPanel.setLayout(mainLayout);
		
//======================================================================leftOptionsPanel
		
		leftSensorPanel = new NikeSensorPanel(Direction.LEFT);
		TitledBorder leftBorder = BorderFactory.createTitledBorder(
				new LineBorder(Color.lightGray), "Left Foot");
		leftSensorPanel.setBorder(leftBorder);
		
		GridBagConstraints leftCons = new GridBagConstraints();
		leftCons.gridx = 1;
		leftCons.gridy = 1;
		leftCons.gridwidth = 1;
		leftCons.weightx = 0.15;
		leftCons.weighty = 0.6;
		leftCons.fill = GridBagConstraints.BOTH;
		leftCons.insets = new Insets(0, 15, 10, 5);
		mainPanel.add(leftSensorPanel, leftCons);
		
//======================================================================rightOptionsPanel

		rightSensorPanel = new NikeSensorPanel(Direction.RIGHT);
		TitledBorder rightBorder = BorderFactory.createTitledBorder(
				new LineBorder(Color.lightGray), "Right Foot");
		rightSensorPanel.setBorder(rightBorder);
		
		GridBagConstraints rightCons = new GridBagConstraints();
		rightCons.gridx = 2;
		rightCons.gridy = 1;
		rightCons.gridwidth = 1;
		rightCons.weightx = 0.15;
		rightCons.weighty = 0.6;
		rightCons.fill = GridBagConstraints.BOTH;
		rightCons.insets = new Insets(0, 5, 10, 15);
		mainPanel.add(rightSensorPanel, rightCons);
		
//======================================================================settingsPanel
		
		settingsPanel = new SettingsPanel();
		TitledBorder settingsBorder = BorderFactory.createTitledBorder(
				new LineBorder(Color.lightGray), "Settings");
		settingsPanel.setBorder(settingsBorder);
		
		GridBagConstraints settingsCons = new GridBagConstraints();
		settingsCons.gridx = 1;
		settingsCons.gridy = 0;
		settingsCons.gridwidth = 2;
		settingsCons.weightx = 0.3;
		settingsCons.weighty = 0.1;
		settingsCons.fill = GridBagConstraints.BOTH;
		settingsCons.insets = new Insets(0, 15, 10, 15);
		mainPanel.add(settingsPanel, settingsCons);
		
//======================================================================recordPanel
		
		JPanel recordPanel = new JPanel(new GridBagLayout());
		recordPanel.setBackground(Color.white);
		TitledBorder recordBorder = BorderFactory.createTitledBorder(
				new LineBorder(Color.lightGray), "Record");
		recordPanel.setBorder(recordBorder);
		
		GridBagConstraints recordCons = new GridBagConstraints();
		recordCons.gridx = 1;
		recordCons.gridy = 2;
		recordCons.gridwidth = 2;
		recordCons.gridheight = 1;
		recordCons.weightx = 0.3;
		recordCons.weighty = 0.4;
		recordCons.fill = GridBagConstraints.BOTH;
		recordCons.insets = new Insets(10, 15, 20, 15);
		mainPanel.add(recordPanel, recordCons);
		
//----------------------------------------------------------------------recordPanelLabels
		
		JLabel recordDirectoryLabel = 
				new JLabel("Record Directory:", JLabel.RIGHT);
		recordDirectoryLabel.setFont(new Font("Times Romam", Font.PLAIN, 13));
		JLabel experimentNameLabel = 
				new JLabel("Experiment Name:", JLabel.RIGHT);
		experimentNameLabel.setFont(new Font("Times Romam", Font.PLAIN, 13));
		JLabel TesterNameLabel = 
				new JLabel("Tester Name:", JLabel.RIGHT);
		TesterNameLabel.setFont(new Font("Times Romam", Font.PLAIN, 13));
		GridBagConstraints labelsCons = new GridBagConstraints();
		labelsCons.gridx = 1;
		labelsCons.gridy = 1;
		labelsCons.weightx = 0.1;
		labelsCons.weighty = 0.2;
		labelsCons.fill = GridBagConstraints.NONE;
		labelsCons.anchor = GridBagConstraints.EAST;
		recordPanel.add(recordDirectoryLabel, labelsCons);
		labelsCons.gridy = 2;
		recordPanel.add(experimentNameLabel, labelsCons);
		labelsCons.gridy = 3;
		recordPanel.add(TesterNameLabel, labelsCons);
	
//----------------------------------------------------------------------recordDirPanel
		
		JPanel recordDirPanel = new JPanel(new BorderLayout());
		GridBagConstraints recordTextCons = new GridBagConstraints();
		recordTextCons.gridx = 2;
		recordTextCons.gridy = 1;
		recordTextCons.weightx = 1.0;
		recordTextCons.weighty = 0.2;
		recordTextCons.fill = GridBagConstraints.HORIZONTAL;
		recordTextCons.anchor = GridBagConstraints.WEST;
		recordTextCons.insets = new Insets(0, 10, 0, 15);
		recordPanel.add(recordDirPanel, recordTextCons);
		
//----------------------------------------------------------------------recordDirText
		
		recordDirText = new JTextField("C:\\Pressure Records\\", 10);
		recordDirText.setFont(new Font("Times Romam", Font.PLAIN, 15));
		recordDirText.setEditable(false);
		recordDirPanel.add(recordDirText, BorderLayout.CENTER);
		
//----------------------------------------------------------------------recordDirButton
		
		recordDirButton = new JButton("...");
		recordDirButton.setFont(new Font("Times Romam", Font.BOLD, 15));
		recordDirPanel.add(recordDirButton, BorderLayout.EAST);
		
//----------------------------------------------------------------------expNameText
		
		expNameText = new JTextField();
		expNameText.setFont(new Font("Times Romam", Font.PLAIN, 15));
		recordTextCons.gridy = 2;
		recordPanel.add(expNameText, recordTextCons);
		
//----------------------------------------------------------------------testerNameText
		
		testerNameText = new JTextField();
		testerNameText.setFont(new Font("Times Romam", Font.PLAIN, 15));
		recordTextCons.gridy = 3;
		recordPanel.add(testerNameText, recordTextCons);
		
//----------------------------------------------------------------------buttonsPanel
		
		JPanel buttonsPanel = new JPanel(new GridBagLayout());
		buttonsPanel.setBackground(Color.white);
		GridBagConstraints buttonsPanelCons = new GridBagConstraints();
		buttonsPanelCons.gridx = 1;
		buttonsPanelCons.gridy = 4;
		buttonsPanelCons.gridwidth = 2;
		buttonsPanelCons.gridheight = 1;
		buttonsPanelCons.weightx = 1.0;
		buttonsPanelCons.weighty = 0.4;
		buttonsPanelCons.fill = GridBagConstraints.BOTH;
		recordPanel.add(buttonsPanel, buttonsPanelCons);
		
//----------------------------------------------------------------------startButton
		
		startButton = new JButton("Start");
		startButton.setFont(new Font("Times Romam", Font.PLAIN, 30));
		GridBagConstraints startButtonCons = new GridBagConstraints();
		startButtonCons.gridx = 1;
		startButtonCons.fill = GridBagConstraints.BOTH;
		startButtonCons.insets = new Insets(0, 10, 0, 10);
		buttonsPanel.add(startButton, startButtonCons);
		
//----------------------------------------------------------------------stopButton
		
		stopButton = new JButton("Stop");
		stopButton.setFont(new Font("Times Romam", Font.PLAIN, 30));
		stopButton.setEnabled(false);
		GridBagConstraints stopButtonCons = new GridBagConstraints();
		stopButtonCons.gridx = 2;
		stopButtonCons.fill = GridBagConstraints.BOTH;
		stopButtonCons.insets = new Insets(0, 10, 0, 10);
		buttonsPanel.add(stopButton, stopButtonCons);
		
//======================================================================monitorPanel

		JPanel monitorPanel = new JPanel(new GridBagLayout());
		monitorPanel.setBackground(Color.white);
//		monitorPanel.setBorder(
//				BorderFactory.createLineBorder(Color.lightGray));
		GridBagConstraints monitorCons = new GridBagConstraints();
		monitorCons.gridx = 3;
		monitorCons.gridy = 0;
		monitorCons.gridwidth = 1;
		monitorCons.gridheight = 3;
		monitorCons.weightx = 5;
		monitorCons.weighty = 1.0;
		monitorCons.fill = GridBagConstraints.BOTH;
		monitorCons.insets = new Insets(0, 0, 0, 15);
		mainPanel.add(monitorPanel, monitorCons);
		
//======================================================================leftMonitorPanel
		
		leftMonitorPanel = new MonitorPanel(Direction.LEFT);
		GridBagConstraints leftMonitorCons = new GridBagConstraints();
		leftMonitorCons.gridx = 1;
		leftMonitorCons.gridy = 1;
		leftMonitorCons.gridwidth = 1;
		leftMonitorCons.gridheight = 1;
		leftMonitorCons.weightx = 0.5;
		leftMonitorCons.weighty = 0.8;
		leftMonitorCons.fill = GridBagConstraints.BOTH;
		monitorPanel.add(leftMonitorPanel, leftMonitorCons);
		
//======================================================================rightMonitorPanel
		
		rightMonitorPanel = new MonitorPanel(Direction.RIGHT);
		GridBagConstraints rightMonitorCons = new GridBagConstraints();
		rightMonitorCons.gridx = 2;
		rightMonitorCons.gridy = 1;
		rightMonitorCons.gridwidth = 1;
		rightMonitorCons.gridheight = 1;
		rightMonitorCons.weightx = 0.5;
		rightMonitorCons.weighty = 0.9;
		rightMonitorCons.fill = GridBagConstraints.BOTH;
		monitorPanel.add(rightMonitorPanel, rightMonitorCons);
		
//======================================================================playerPanel
		
		this.playerPanel = new PlayerPanel();
		playerPanel.setBackground(new Color(0x10, 0x10, 0x15));
		GridBagConstraints playerCons = new GridBagConstraints();
		playerCons.gridx = 1;
		playerCons.gridy = 2;
		playerCons.gridwidth = 2;
		playerCons.gridheight = 1;
		playerCons.weightx = 1.0;
		playerCons.fill = GridBagConstraints.BOTH;
		playerCons.insets = new Insets(0, 0, 20, 0);
		monitorPanel.add(playerPanel, playerCons);
		
		this.add(mainPanel);
		addListeners();
	}
	
	/**
	 * �[�J�U���󪺨ƥ�
	 */
	private void addListeners() {
		
		//�}�ҰO���ɦ�m�������A�u�����Ƨ�
		recordDirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				File currentDirectory = 
						new File(recordDirText.getText());
				fileChooser.setCurrentDirectory(currentDirectory);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setDialogTitle("Choose records directory");
				if (fileChooser.showOpenDialog(MainFrame.this) == 
						JFileChooser.APPROVE_OPTION) {
					recordDirText.setText(
							fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		//�}�l�O���ƭȡA�Y�P�����|���s���h���ʧ@�A�_�h�}�l�O���A�æb�����e���W������O���߲��ʭy��C
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NikeSensor leftSensor = leftSensorPanel.getSensor();
				NikeSensor rightSensor = rightSensorPanel.getSensor();
				
				if ((leftSensor == null) || (rightSensor == null)) {
					return;
				}
				
				if (!(settingsPanel.isConnected())) {
					return;
				}
				
				try {
					leftSensor.setSensorPoints(leftSensorPanel.getSensorPoints());
					rightSensor.setSensorPoints(rightSensorPanel.getSensorPoints());
					dataRecorder = new NikeDataRecorder(leftSensor, rightSensor);
					dataRecorder.start(getRecordInfo());
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
					leftMonitorPanel.monitorPanel.startRecordCoP();
					rightMonitorPanel.monitorPanel.startRecordCoP();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//����O���ƭȡA�ð���������O���߭y��A�u�X�O�����x�s��m���T���C
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataRecorder.stop();
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
				leftMonitorPanel.monitorPanel.stopRecordCoP();
				rightMonitorPanel.monitorPanel.stopRecordCoP();
				JOptionPane.showMessageDialog(MainFrame.this, 
						"Saved record to " + dataRecorder.getRecordPath());
			}
		});
	}
	
	/**
	 * ��l�Ƶ����A��Ū���{���һݸ�ơA�]�A�c�Ԭ�����ơB����Logo�B���O�P�����ƭȼ����e�����Ϥ��B
	 * �c�Ԥ��P�j�p���P������ڮy��
	 * 
	 * @throws IOException
	 */
	private void init() throws IOException {
		this.setSize(1024, 768);
		NikeSensorResource.loadImageResources(this);
		PlayerResource.load(this);
		this.setIconImage(ImageIO.read(new File("res\\logo.png")));
		this.shoeBaseSizeLoader = new ShoeBaseSizeLoader();
		this.shoeBaseSizeLoader.load("res/size.txt");
	}
	
	/**
	 * ���o�ϥΪ̦b���W��J���O����ƩҲ��ͪ�NikeRecordInfo����
	 * 
	 * @return NikeRecordInfo
	 */
	private NikeRecordInfo getRecordInfo() {
		NikeRecordInfo recordInfo = new NikeRecordInfo();
		recordInfo.setDirectory(new File(recordDirText.getText()));
		if (expNameText.getText().replace(" ", "").length() > 0) {
			recordInfo.setRecordName(expNameText.getText());
		}
		if (testerNameText.getText().replace(" ", "").length() > 0) {
			recordInfo.setTester(testerNameText.getText());
		}
		return recordInfo;
	}
	
	/**
	 * �]�w�s���P������Panel�A�ΥH�s��ComPort�B�]�w�c�Ԥj�p�B��}��ƥ洫
	 * 
	 * @author mingjia
	 *
	 */
	class SettingsPanel extends JPanel {
		
		private static final long serialVersionUID = -6174875345226980515L;
		
		/**
		 * ���ComPort���U�Ԧ����
		 */
		JComboBox<String> comPortList;
		
		/**
		 * �s���ҿ��ComPort�����s
		 */
		JButton comPortButton;
		
		/**
		 * ��ܾc�Ԥj�p���U�Ԧ����A�@����ܫ�}�l�O���ɪ����M��
		 */
		JComboBox<String> sizeList;
		
		/**
		 * ��}��Ƥ��������s
		 */
		JButton swapButton;
		
		/**
		 * �P�����s����
		 */
		NikeSensorConnector connector;
		
		/**
		 * �إ߳]�w��Panel
		 */
		public SettingsPanel() {
			this.setBackground(Color.white);
			this.setLayout(new GridBagLayout());
			
//======================================================================comPortPanel
			
			JPanel comPortPanel = new JPanel(new BorderLayout());
			comPortPanel.setBackground(Color.white);
			
			GridBagConstraints comPortPanelCons = new GridBagConstraints();
			comPortPanelCons.gridx = 1;
			comPortPanelCons.gridy = 1;
			comPortPanelCons.gridwidth = 1;
			comPortPanelCons.gridheight = 2;
			comPortPanelCons.weightx = 1;
			comPortPanelCons.weighty = 1;
			comPortPanelCons.fill = GridBagConstraints.HORIZONTAL;
			comPortPanelCons.insets = new Insets(0, 10, 0 ,10);
			this.add(comPortPanel, comPortPanelCons);
			
//----------------------------------------------------------------------comPortLabel
			
			JLabel comPortLabel = new JLabel("Comm-Port");
			comPortLabel.setFont(new Font("Times Romam", Font.BOLD, 15));
			comPortPanel.add(comPortLabel, BorderLayout.NORTH);
			
//----------------------------------------------------------------------comPortList
			
			comPortList = new JComboBox<String>();
			comPortPanel.add(comPortList, BorderLayout.CENTER);
			
//----------------------------------------------------------------------comPortButton
			
			comPortButton = new JButton("Connect");
			comPortPanel.add(comPortButton, BorderLayout.SOUTH);
			
//======================================================================sizePanel
			
			JPanel sizePanel = new JPanel(new BorderLayout());
			sizePanel.setBackground(Color.white);
			
			GridBagConstraints sizePanelCons = new GridBagConstraints();
			sizePanelCons.gridx = 2;
			sizePanelCons.gridy = 1;
			sizePanelCons.gridwidth = 1;
			sizePanelCons.gridheight = 1;
			sizePanelCons.weightx = 1;
			sizePanelCons.weighty = 0.5;
			sizePanelCons.fill = GridBagConstraints.HORIZONTAL;
			sizePanelCons.insets = new Insets(0, 10, 0 ,10);
			this.add(sizePanel, sizePanelCons);
		
//----------------------------------------------------------------------sizeLabel
			
			JLabel sizeLabel = new JLabel("Size");
			sizeLabel.setFont(new Font("Times Romam", Font.BOLD, 15));
			
			sizePanel.add(sizeLabel, BorderLayout.NORTH);
			
//----------------------------------------------------------------------sizeList
			
			sizeList = new JComboBox<String>(shoeBaseSizeLoader.getSizeNameList());
			
			sizePanel.add(sizeList, BorderLayout.CENTER);
			
//======================================================================swapButton
			
			swapButton = new JButton("L/R Swap");
			
			GridBagConstraints swapButtonCons = new GridBagConstraints();
			
			swapButtonCons.gridx = 2;
			swapButtonCons.gridy = 2;
			swapButtonCons.gridwidth = 1;
			swapButtonCons.gridheight = 1;
			swapButtonCons.weightx = 1;
			swapButtonCons.weighty = 0.5;
			swapButtonCons.fill = GridBagConstraints.HORIZONTAL;
			swapButtonCons.insets = new Insets(0, 10, 0 ,10);
			
			this.add(swapButton, swapButtonCons);
			
			init();
			addListeners();
		}
		
		/**
		 * �[�J�U���󪺨ƥ�
		 */
		private void addListeners() {
			
			//�I��ComPort���U�Ԧ����᭫�s���y�ç�sComPort�M��
			comPortList.addPopupMenuListener(new PopupMenuListener() {
				@Override
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
					refreshComPortList();
				}
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
				public void popupMenuCanceled(PopupMenuEvent e) {}
			});
			
			//�P�P�����s���Ϊ��_�u
			comPortButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if ("Connect".equals(comPortButton.getText())) {
						connect();
					} else {
						disconnect();
					}
				}
			});
			
			//��}��Ƥ���
			swapButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					connector.swapDataGetter();
				}
			});
		}
		
		/**
		 * �P�P�����s���A�Y�s�����ѫh���s���y�ç�sComPort�M��
		 */
		private void connect() {
			try {
				if (connector.isConnected()) {
					refreshComPortList();
				} else {
					connector.connect((String) comPortList.getSelectedItem());
					comPortList.setEnabled(false);
					comPortButton.setText("Disconnect");
				}
				
				NikeSensor leftSensor = new NikeSensor(Direction.LEFT);
				connector.setLeftDataGetter(leftSensor);
				leftSensorPanel.setSensor(leftSensor);
				leftMonitorPanel.setDataGetter(leftSensor);
				leftSensor.setSensorPoints(leftSensorPanel.getSensorPoints());
				
				NikeSensor rightSensor = new NikeSensor(Direction.RIGHT);
				connector.setRightDataGetter(rightSensor);
				rightSensorPanel.setSensor(rightSensor);
				rightMonitorPanel.setDataGetter(rightSensor);
				rightSensor.setSensorPoints(rightSensorPanel.getSensorPoints());
				
			} catch (Exception e1) {
				refreshComPortList();
			}
		}
		
		/**
		 * �P�P�����_�}�s��
		 */
		private void disconnect() {
			if (connector.isConnected()) {
				connector.disconnect();
			}
			comPortList.setEnabled(true);
			comPortButton.setText("Connect");
			leftSensorPanel.removeSensor();
			rightSensorPanel.removeSensor();
		}
		
		/**
		 * �O�_�w�s���ܷP����
		 * 
		 * @return is or not connected to sensor
		 */
		public boolean isConnected() {
			return connector.isConnected();
		}
		
		/**
		 * ��l�Ƴ]�wPanel�A���yComPort�M��
		 */
		private void init() {
			connector = new NikeSensorConnector();
			refreshComPortList();
		}
		
		/**
		 * ���˨í��s��zComPort�M��
		 */
		private void refreshComPortList() {
			List<String> ports = listPorts();
			comPortList.removeAllItems();
			for (String portName : ports) {
				comPortList.addItem(portName);
			}
		}
		
		/**
		 * ���o�q�������Ҧ��s����ComPort���˸m
		 * 
		 * @return ArrayList<String>
		 */
	    @SuppressWarnings("unchecked")
		private ArrayList<String> listPorts() {
	        ArrayList<String> ports = new ArrayList<String>();
	        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
	        while (portEnum.hasMoreElements()) {
	            CommPortIdentifier protId = portEnum.nextElement();
	            if (!(protId.isCurrentlyOwned())) {
		            ports.add(protId.getName());
	            }
	        }
	        return ports;
	    }

	    /**
	     * ���o�P�ҿ�ܾc�Ԥj�p�۹������y�и��
	     * 
	     * @return SensorPoints
	     */
		public SensorPoints getSensorPoints() {
			String size = (String) sizeList.getSelectedItem();
			return shoeBaseSizeLoader.getSensorPoints(size);
		}
	    
	}
	
	/**
	 * �Y����ܷP�����ƭȪ�Panel�A�u�t�d��ܳ�}�P�������ƭ�
	 * 
	 * @author mingjia
	 *
	 */
	class NikeSensorPanel extends JPanel implements NikeDataListener {
		
		private static final long serialVersionUID = 3673482960974572470L;
	
		/**
		 * ���X�b�[�t�׷P�����ƭȪ���r���
		 */
		JTextField xText;

		/**
		 * ���Y�b�[�t�׷P�����ƭȪ���r���
		 */
		JTextField yText;

		/**
		 * ���Z�b�[�t�׷P�����ƭȪ���r���
		 */
		JTextField zText;

		/**
		 * ���A�I���O�P�����ƭȪ���r���
		 */
		JTextField aText;

		/**
		 * ���B�I���O�P�����ƭȪ���r���
		 */
		JTextField bText;

		/**
		 * ���C�I���O�P�����ƭȪ���r���
		 */
		JTextField cText;

		/**
		 * ���D�I���O�P�����ƭȪ���r���
		 */
		JTextField dText;
		
		/**
		 * �ƭȸ�ƨӷ�
		 */
		NikeSensor sensor;
		
		/**
		 * �P��������V
		 */
		Direction direction;
		
		/**
		 * ��ܼƭȤ�r���r��
		 */
		private Font dataDisplayFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15); 
		
		/**
		 * �إߧY����ܷP�����ƭȪ�Panel�A���]�w�Ҫ�ܪ���V
		 * 
		 * @param direction
		 */
		public NikeSensorPanel(Direction direction) {
			super(new GridBagLayout());
			this.setBackground(Color.white);
			
			this.direction = direction;
			
//======================================================================gForceLabel
			
			JLabel gForceLabel = new JLabel("G-Force (m/s^2)");
			gForceLabel.setFont(new Font("Times Romam", Font.BOLD, 15));
			
			GridBagConstraints gForceLabelCons = new GridBagConstraints();
			gForceLabelCons.gridx = 1;
			gForceLabelCons.gridy = 4;
			gForceLabelCons.gridwidth = 2;
			gForceLabelCons.gridheight = 1;
			gForceLabelCons.weightx = 1.0;
			gForceLabelCons.weighty = 0.1;
			gForceLabelCons.fill = GridBagConstraints.HORIZONTAL;
			gForceLabelCons.anchor = GridBagConstraints.WEST;
			gForceLabelCons.insets = new Insets(0, 10, 0 ,10);
			this.add(gForceLabel, gForceLabelCons);
			
//======================================================================gForceLabels
			
			JLabel xLabel = new JLabel("X", JLabel.CENTER);
			JLabel yLabel = new JLabel("Y", JLabel.CENTER);
			JLabel zLabel = new JLabel("Z", JLabel.CENTER);
			
			GridBagConstraints gForceLabelsCons = new GridBagConstraints();
			gForceLabelsCons.gridx = 1;
			gForceLabelsCons.gridy = 5;
			gForceLabelsCons.gridwidth = 1;
			gForceLabelsCons.gridheight = 1;
			gForceLabelsCons.weightx = 0.3;
			gForceLabelsCons.weighty = 0.1;
			gForceLabelsCons.fill = GridBagConstraints.HORIZONTAL;
			gForceLabelsCons.anchor = GridBagConstraints.EAST;
			this.add(xLabel, gForceLabelsCons);
			
			gForceLabelsCons.gridy = 6;
			this.add(yLabel, gForceLabelsCons);
			
			gForceLabelsCons.gridy = 7;
			this.add(zLabel, gForceLabelsCons);
			
//======================================================================gForceText
			
			xText = new JTextField();
			xText.setEditable(false);
			xText.setBackground(Color.white);
			xText.setFont(dataDisplayFont);
			
			yText = new JTextField();
			yText.setEditable(false);
			yText.setBackground(Color.white);
			yText.setFont(dataDisplayFont);
			
			zText = new JTextField();
			zText.setEditable(false);
			zText.setBackground(Color.white);
			zText.setFont(dataDisplayFont);
			
			GridBagConstraints gForceTextCons = new GridBagConstraints();
			gForceTextCons.gridx = 2;
			gForceTextCons.gridy = 5;
			gForceTextCons.gridwidth = 1;
			gForceTextCons.gridheight = 1;
			gForceTextCons.weightx = 0.7;
			gForceTextCons.weighty = 0.1;
			gForceTextCons.fill = GridBagConstraints.HORIZONTAL;
			gForceTextCons.insets = new Insets(0, 0, 0 , 10);
			this.add(xText, gForceTextCons);
			
			gForceTextCons.gridy = 6;
			this.add(yText, gForceTextCons);
			
			gForceTextCons.gridy = 7;
			this.add(zText, gForceTextCons);
			
//======================================================================pressureLabel
			
			JLabel pressureLabel = new JLabel("Pressure");
			pressureLabel.setFont(new Font("Times Romam", Font.BOLD, 15));
			
			GridBagConstraints pressureLabelCons = new GridBagConstraints();
			pressureLabelCons.gridx = 1;
			pressureLabelCons.gridy = 8;
			pressureLabelCons.gridwidth = 2;
			pressureLabelCons.gridheight = 1;
			pressureLabelCons.weightx = 1.0;
			pressureLabelCons.weighty = 0.1;
			pressureLabelCons.fill = GridBagConstraints.HORIZONTAL;
			pressureLabelCons.anchor = GridBagConstraints.WEST;
			pressureLabelCons.insets = new Insets(0, 10, 0 ,10);
			this.add(pressureLabel, pressureLabelCons);
			
//======================================================================pressureLabels
			
			JLabel aLabel = new JLabel("A", JLabel.CENTER);
			JLabel bLabel = new JLabel("B", JLabel.CENTER);
			JLabel cLabel = new JLabel("C", JLabel.CENTER);
			JLabel dLabel = new JLabel("D", JLabel.CENTER);
			
			GridBagConstraints pressureLabelsCons = new GridBagConstraints();
			pressureLabelsCons.gridx = 1;
			pressureLabelsCons.gridy = 9;
			pressureLabelsCons.gridwidth = 1;
			pressureLabelsCons.gridheight = 1;
			pressureLabelsCons.weightx = 0.3;
			pressureLabelsCons.weighty = 0.1;
			pressureLabelsCons.fill = GridBagConstraints.HORIZONTAL;
			pressureLabelsCons.anchor = GridBagConstraints.EAST;
			this.add(aLabel, pressureLabelsCons);
			
			pressureLabelsCons.gridy = 10;
			this.add(bLabel, pressureLabelsCons);
			
			pressureLabelsCons.gridy = 11;
			this.add(cLabel, pressureLabelsCons);
			
			pressureLabelsCons.gridy = 12;
			this.add(dLabel, pressureLabelsCons);
			
//======================================================================preesureText
			
			aText = new JTextField();
			aText.setEditable(false);
			aText.setBackground(Color.white);
			aText.setFont(dataDisplayFont);
			
			bText = new JTextField();
			bText.setEditable(false);
			bText.setBackground(Color.white);
			bText.setFont(dataDisplayFont);
			
			cText = new JTextField();
			cText.setEditable(false);
			cText.setBackground(Color.white);
			cText.setFont(dataDisplayFont);
			
			dText = new JTextField();
			dText.setEditable(false);
			dText.setBackground(Color.white);
			dText.setFont(dataDisplayFont);
			
			GridBagConstraints preesureTextCons = new GridBagConstraints();
			preesureTextCons.gridx = 2;
			preesureTextCons.gridy = 9;
			preesureTextCons.gridwidth = 1;
			preesureTextCons.gridheight = 1;
			preesureTextCons.weightx = 0.7;
			preesureTextCons.weighty = 0.1;
			preesureTextCons.fill = GridBagConstraints.HORIZONTAL;
			preesureTextCons.insets = new Insets(0, 0, 0 , 10);
			this.add(aText, preesureTextCons);
			
			preesureTextCons.gridy = 10;
			this.add(bText, preesureTextCons);
			
			preesureTextCons.gridy = 11;
			this.add(cText, preesureTextCons);
			
			preesureTextCons.gridy = 12;
			this.add(dText, preesureTextCons);
		}
		
		/**
		 * �ǰe��ƨç�s�U��r������ܪ��ƭ�
		 */
		@Override
		public void onDataCallBack(float[] signals, Direction direction) {
			xText.setText(String.format("%.4f", signals[0]));
			yText.setText(String.format("%.4f", signals[1]));
			zText.setText(String.format("%.4f", signals[2]));
			aText.setText(String.format("%.0f", signals[3]));
			bText.setText(String.format("%.0f", signals[4]));
			cText.setText(String.format("%.0f", signals[5]));
			dText.setText(String.format("%.0f", signals[6]));
		}
		
		/**
		 * �]�w��ƨӷ�
		 * 
		 * @param NikeSensor
		 */
		public void setSensor(NikeSensor sensor) {
			this.sensor = sensor;
			this.sensor.addDataListener(this);
		}
		
		/**
		 * ������ƨӷ�
		 */
		public void removeSensor() {
			this.sensor.removeDataListener(this);
			this.sensor = null;
		}
		
		/**
		 * ���o��ƨӷ�
		 * 
		 * @return NikeSensor
		 */
		public NikeSensor getSensor() {
			return this.sensor;
		}
		
		/**
		 * ���o�P�ҿ�ܾc�Ԥj�p�۹������y�и��(from settingsPanel of MainFrame)
		 * 
		 * @return SensorPoints
		 */
		public SensorPoints getSensorPoints() {
			return settingsPanel.getSensorPoints();
		}
		
		public void clearSensersText() {
			xText.setText("");
			yText.setText("");
			zText.setText("");
			aText.setText("");
			bText.setText("");
			cText.setText("");
			dText.setText("");
		}
		
	}
	
	/**
	 * �Y�ɼ������O�P�����ƭȪ�Panel
	 * 
	 * @author mingjia
	 *
	 */
	class MonitorPanel extends JPanel {

		private static final long serialVersionUID = 4829130741998670607L;
		
		/**
		 * �Y�ɼ������O�P�����ƭȪ�Panel
		 */
		NikeSensorMonitorPanel monitorPanel;
		
		/**
		 * �إߧY�ɼ������O�P�����ƭȪ�Panel�A���]�w����ܪ���V
		 * 
		 * @param direction
		 */
		public MonitorPanel(Direction direction) {
			this.setLayout(new BorderLayout());
			this.setBackground(Color.black);
			this.monitorPanel = new NikeSensorMonitorPanel(direction);
			this.add(monitorPanel, BorderLayout.CENTER);
		}
		
		/**
		 * �]�w��ƨӷ�
		 * 
		 * @param dataGetter
		 */
		public void setDataGetter(NikeDataGetter dataGetter) {
			this.monitorPanel.setDataGetter(dataGetter);
		}
		
		/**
		 * ������ƨӷ�
		 */
		public void removeDataGetter() {
			this.monitorPanel.removeDataGetter();
		}
		
	}
	
	/**
	 * �O�����񾹿ﶵ��Panel
	 * 
	 * @author mingjia
	 *
	 */
	class PlayerPanel extends JPanel implements EndPlayingListener {
	
		private static final long serialVersionUID = 5458534423454431813L;
		
		/**
		 * �O�����񪺶i�׮ɶ���
		 */
		TimeSlider timeBar;
		
		/**
		 * ������s
		 */
		JButton playButton;
		
		/**
		 * ����������s
		 */
		JButton forwardButton;
		
		/**
		 * ���s������s
		 */
		JButton rewindButton;
		
		/**
		 * �[�ּ���t�׫��s
		 */
		JButton fastForwardButton;
		
		/**
		 * ��C����t�׫��s
		 */
		JButton fastBackButton;
		
		/**
		 * ��^�D�e�����s
		 */
		JButton leftButton;
		
		/**
		 * �}�ҰO���ɿ����s
		 */
		JButton folderButton;
		
		RecordDataGetter leftRecordDataGetter;
		RecordDataGetter rightRecordDataGetter;
		NikeDataPlayer dataPlayer;
		
		boolean isPlaying;
		
		public PlayerPanel() {
			init();
			this.setLayout(new GridBagLayout());
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			
			this.timeBar = new TimeSlider();
			GridBagConstraints timeBarCons = new GridBagConstraints();
			this.add(timeBar, gbc);
			
			
			JPanel controllPanel = new JPanel(new GridLayout(1, 5));
			gbc.gridy = 2;
			this.add(controllPanel, gbc);

			this.leftButton = createButton("left-arrow");
			this.leftButton.setToolTipText("Return to immediate mode");
			controllPanel.add(leftButton);
			
			this.rewindButton = createButton("rewind");
			this.rewindButton.setToolTipText("Replay");
			controllPanel.add(rewindButton);
			
			this.fastBackButton = createButton("fast-back");
			this.fastBackButton.setToolTipText("Slow down");
			controllPanel.add(fastBackButton);
			
			this.playButton = createButton("play");
			this.playButton.setToolTipText("Play");
			controllPanel.add(playButton);
			
			this.fastForwardButton = createButton("fast-forward");
			this.fastForwardButton.setToolTipText("Speed up");
			controllPanel.add(fastForwardButton);
			
			this.forwardButton = createButton("forward");
			this.forwardButton.setToolTipText("End play");
			controllPanel.add(forwardButton);

			this.folderButton = createButton("folder");
			this.folderButton.setToolTipText("Open record");
			controllPanel.add(folderButton);
			
			addListeners();
			setEnabled(false);
		}
		
		private JButton createButton(String icon) {
			JButton button = new JButton();
			button.setIcon(PlayerResource.getIcon(icon));
			button.setRolloverIcon(PlayerResource.getIcon(icon + "-rollover"));
			button.setPressedIcon(PlayerResource.getIcon(icon + "-selected"));
			button.setFocusable(false);
			button.setBorder(null);
			button.setContentAreaFilled(false);
			button.setOpaque(true);
			button.setBackground(Color.black);
			return button;
		}
		
		public void addListeners() {
			this.folderButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser();
					File currentDirectory = 
							new File(recordDirText.getText());
					fileChooser.setCurrentDirectory(currentDirectory);
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.setDialogTitle("Choose record file");
					if (fileChooser.showOpenDialog(MainFrame.this) == 
							JFileChooser.APPROVE_OPTION) {
						try {
							File recordFile = fileChooser.getSelectedFile();
							NikeDataLoader dataLoader = new NikeDataLoader(recordFile);
							
							leftRecordDataGetter = new RecordDataGetter(Direction.LEFT);
							leftRecordDataGetter.setSensorPoints(leftSensorPanel.getSensorPoints());
							leftMonitorPanel.setDataGetter(leftRecordDataGetter);
							if (leftSensorPanel.getSensor() != null) {
								leftSensorPanel.getSensor().removeDataListener(leftSensorPanel);
							}
							leftRecordDataGetter.addDataListener(leftSensorPanel);
							
							rightRecordDataGetter = new RecordDataGetter(Direction.RIGHT);
							rightRecordDataGetter.setSensorPoints(rightSensorPanel.getSensorPoints());
							rightMonitorPanel.setDataGetter(rightRecordDataGetter);
							if (rightSensorPanel.getSensor() != null) {
								rightSensorPanel.getSensor().removeDataListener(rightSensorPanel);
							}
							rightRecordDataGetter.addDataListener(rightSensorPanel);
							
							dataPlayer = new NikeDataPlayer(dataLoader);
							dataPlayer.setDataGetter(leftRecordDataGetter, rightRecordDataGetter);
							setEnabled(true);
							dataPlayer.addEndPlayingListener(PlayerPanel.this);
							timeBar.setMaximum(dataPlayer.getLength());
							timeBar.setValue(0);
							dataPlayer.addPlayTimeListener(timeBar);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(MainFrame.this, 
									"Failed to Loading record file");
							ex.printStackTrace();
						}
						
					}
				}
			});
			this.playButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (isPlaying) {
						dataPlayer.stop();
						setButtonIcon(playButton, "play");
						isPlaying = false;
					} else {
						dataPlayer.start();
						setButtonIcon(playButton, "pause");
						isPlaying = true;
						leftMonitorPanel.monitorPanel.restartRecordCoP();
						rightMonitorPanel.monitorPanel.restartRecordCoP();
					}
				}
			});
			this.leftButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dataPlayer.end();
					dataPlayer.close();
					
					leftMonitorPanel.setDataGetter(leftSensorPanel.sensor);
					leftMonitorPanel.monitorPanel.stopRecordCoP();
					leftRecordDataGetter.removeDataListener(leftSensorPanel);
					leftSensorPanel.clearSensersText();
					if (leftSensorPanel.getSensor() != null) {
						leftSensorPanel.getSensor().addDataListener(leftSensorPanel);
					}
					
					rightMonitorPanel.setDataGetter(rightSensorPanel.sensor);
					rightMonitorPanel.monitorPanel.stopRecordCoP();
					rightRecordDataGetter.removeDataListener(rightSensorPanel);
					leftSensorPanel.clearSensersText();
					if (rightSensorPanel.getSensor() != null) {
						rightSensorPanel.getSensor().addDataListener(rightSensorPanel);
					}
					
					onPlayingEnd();
					setEnabled(false);
				}
			});
			this.fastBackButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dataPlayer.speedDown();
				}
			});
			this.fastForwardButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dataPlayer.speedUp();
				}
			});
			this.rewindButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dataPlayer.end();
					timeBar.setValue(0);
				}
			});
			this.forwardButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dataPlayer.end();
				}
			});
		}
		
		public void onPlayingEnd() {
			setButtonIcon(playButton, "play");
			isPlaying = false;
			this.timeBar.setValue(0);
		}
		
		private void setButtonIcon(JButton button, String icon) {
			button.setIcon(PlayerResource.getIcon(icon));
			button.setRolloverIcon(PlayerResource.getIcon(icon + "-rollover"));
			button.setPressedIcon(PlayerResource.getIcon(icon + "-selected"));
		}
		
		public void init() {
			isPlaying = false;
		}
		
		public void setEnabled(boolean enable) {
			this.timeBar.setEnabled(enable);
			this.playButton.setEnabled(enable);
			this.rewindButton.setEnabled(enable);
			this.forwardButton.setEnabled(enable);
			this.fastForwardButton.setEnabled(enable);
			this.fastBackButton.setEnabled(enable);
			this.leftButton.setEnabled(enable);
		}
		
		class TimeSlider extends JSlider implements PlayTimeListener {
			
			private static final long serialVersionUID = -3413959367401774874L;

			public TimeSlider() {
				super(HORIZONTAL, 0, 1, 0);
				this.setOpaque(true);
				this.setBackground(Color.black);
				this.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						dataPlayer.setCurrentTime(TimeSlider.this.getValue());
					}
				});
			}

			public void tick(int time) {
				this.setValue(time);
			}
			
		}
		
	}
	
}
