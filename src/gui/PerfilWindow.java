package gui;

import java.awt.EventQueue;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entities.Agenda;
import entities.Compromisso;
import entities.Sessao;
import entities.Usuario;
import service.AgendaService;
import service.CompromissoService;
import service.NotificacaoService;
import service.UsuarioService;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class PerfilWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private UsuarioService usuarioService;
	private AgendaService agendaService;
	private Usuario sessao; 
	private JTable table;
	private JTable tableAgenda;
	private CompromissoService compromissoService;
	private JLabel lblFotoPerfil;
	
	private JComboBox comboBoxAgendas;
    private JButton uploadButton;
    
    private File selectedFile;
    private List<Agenda> listaAgenda;
    
    
	public PerfilWindow(Usuario sessao){
		
		NotificacaoService notificacaoService = new NotificacaoService(sessao);
		
		
		  Thread serviceThread = new Thread(notificacaoService);
		  serviceThread.start();
		  
		

		agendaService = new AgendaService();
		usuarioService = new UsuarioService();
		compromissoService = new CompromissoService();
		
		try {
			
			this.sessao = sessao;
			
			 
			//if(sessao.getEmail()==null) this.sessao = usuarioService.visualizarUsuario(sessao.getIdUsuario());
			//else this.sessao = sessao;
			
			initComponents();

			this.listaAgenda = agendaService.buscarAgendas(Sessao.getUsuario().getIdUsuario());
			buscarAgendas();	
			
			if(sessao.getImagemPerfil()!=null) {
				lblFotoPerfil.setIcon(converterBytesParaIcon(sessao.getImagemPerfil()));
			}
			
			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
	
		}
		
	}
	
	public ImageIcon converterBytesParaIcon(byte[] bytes) throws IOException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
			BufferedImage bufferedImage = ImageIO.read(bais);
			return new ImageIcon(bufferedImage);
		}
	}

	
	public void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 499, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 10, 463, 144);
		contentPane.add(panel);
		
		JLabel lblNomeCompleto = new JLabel("Nome: " + this.sessao.getNomeCompleto());
		lblNomeCompleto.setBounds(10, 17, 173, 14);
		
		JLabel lblEmail = new JLabel("Email: " +this.sessao.getEmail());
		lblEmail.setBounds(10, 79, 173, 14);
		
		JLabel lblDataDeNascimento = new JLabel("Data de nascimento: " +this.sessao.getDataNascimento().toString());
		lblDataDeNascimento.setBounds(10, 110, 173, 14);
		
		JLabel lblGenero = new JLabel("Genero: " + retornarGenero());
		lblGenero.setBounds(10, 48, 173, 14);
		panel.setLayout(null);
		panel.add(lblNomeCompleto);
		panel.add(lblEmail);
		panel.add(lblDataDeNascimento);
		panel.add(lblGenero);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(207, 17, 133, 116);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		lblFotoPerfil = new JLabel("");
		lblFotoPerfil.setBounds(0, 0, 133, 116);
		panel_2.add(lblFotoPerfil);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(352, 11, 101, 125);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnAtualizarPerfil = new JButton("Editar perfil");
		btnAtualizarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new CadastrarUsuarioWindow().setVisible(true);
				dispose();
			}
		});
		btnAtualizarPerfil.setBounds(0, 26, 101, 23);
		panel_1.add(btnAtualizarPerfil);
		//		
		//		JButton btnDeletarPerfil = new JButton("Deletar perfil");
		//		btnDeletarPerfil.setBounds(0, 37, 119, 23);
		//		panel_1.add(btnDeletarPerfil);
				
				JButton btnDeslogar = new JButton("Deslogar");
				btnDeslogar.setBounds(0, 75, 101, 23);
				panel_1.add(btnDeslogar);
		
//		JButton btnAtualizarImagem = new JButton("AtualizarImagem");
//		btnAtualizarImagem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//				try {
//					
//					JFileChooser fileChooser = new JFileChooser();
//	                int returnValue = fileChooser.showOpenDialog(null);
//	                if (returnValue == JFileChooser.APPROVE_OPTION) {
//	                    selectedFile = fileChooser.getSelectedFile();
//	                    usuarioService.AtualizarImagem(selectedFile, sessao);
//	                    lblFotoPerfil.setIcon(new ImageIcon(usuarioService.pegarImagem(sessao)));
//	                }
//	                
//				}catch(Exception ex) {
//					JOptionPane.showMessageDialog(null, ex.getMessage(), "Atualizar foto de perfil", JOptionPane.WARNING_MESSAGE);
//				}
//				
//				
//			}
//		});
//		btnAtualizarImagem.setBounds(0, 101, 119, 23);
//		panel_1.add(btnAtualizarImagem);
		
		JButton btnCompromissos = new JButton("Configurar compromissos");
		btnCompromissos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configurarCompromissos();
			}
		});
		btnCompromissos.setBounds(10, 374, 463, 23);
		contentPane.add(btnCompromissos);
		
		JButton btnAgenda = new JButton("Configurar agendas");
		
		btnAgenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirAgenda();
			}
		});
		btnAgenda.setBounds(256, 165, 194, 23);
		contentPane.add(btnAgenda);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 191, 463, 172);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id","Titulo", "Data de inicio"
			}
		));
		
//		JScrollPane scrollPane_1 = new JScrollPane();
//		scrollPane_1.addMouseListener(new MouseAdapter() {
//			
//		});
//		scrollPane_1.setBounds(281, 264, 194, 99);
//		contentPane.add(scrollPane_1);
		
//		tableAgenda = new JTable();
//		tableAgenda.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				buscarCompromissos();
//			}
//		});	scrollPane_1.setViewportView(tableAgenda);
//		tableAgenda.setModel(new DefaultTableModel(
//			new Object[][] {
//			},
//			new String[] {
//				"id", "Agenda"
//			}
//		));
		//ENVIAR CONVITES
//		JButton btnEnviarConvites = new JButton("Enviar convites");
//		btnEnviarConvites.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			
//				if(table.getSelectedRow()!=-1) {
//					new ConvitesWindow(listaAgenda).setVisible(true);
//					dispose();
//				}else {
//					JOptionPane.showMessageDialog(null, "Nenhum compromisso selecionado", "Convites", JOptionPane.WARNING_MESSAGE);
//				}
//			}
//		});
//		btnEnviarConvites.setBounds(10, 408, 194, 23);
//		contentPane.add(btnEnviarConvites);
//		
		JButton btnVerConvites = new JButton("Ver convites");
		btnVerConvites.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
					new ConvitesWindow(listaAgenda).setVisible(true);
					dispose();
			
				
			
			}
		});
		btnVerConvites.setBounds(10, 408, 463, 23);
		contentPane.add(btnVerConvites);
		
		comboBoxAgendas = new JComboBox();
		comboBoxAgendas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarCompromissos();
			}
		});
	
		comboBoxAgendas.setBounds(31, 165, 194, 22);
		contentPane.add(comboBoxAgendas);
		
		
	}
	
	private void abrirAgenda() {
		new AgendaWindow().setVisible(true);
		dispose();
	}
	
	private String retornarGenero() {
				if(sessao.getGenero().equals("feminino")) {
					return "Feminino";
				}else {
					return "Masculino";
				}
	}
	
	
	
	public void buscarAgendas() {
	    comboBoxAgendas.removeAllItems(); 
	    
	    List<Agenda> agendaLista;
	    try {
	        agendaLista = this.agendaService.buscarAgendas(sessao.getIdUsuario());
	        
	        for (Agenda agenda : agendaLista) {
	     
	            comboBoxAgendas.addItem(agenda.getNome()); 
	        }
	      
	       
	    } catch (IOException | SQLException e) {
	        JOptionPane.showMessageDialog(null, e.getMessage());
	    }
	}
	
//	public void buscarAgendas() {
//		
//		DefaultTableModel modelo = (DefaultTableModel) tableAgenda.getModel();
//		
//		modelo.fireTableDataChanged();
//		
//		modelo.setRowCount(0);
//		
//		
//		List<Agenda> agendaLista;
//		try {
//			agendaLista = this.agendaService.listaAgendas(sessao.getId());
//			
//			for(Agenda agenda : agendaLista) {
//				agenda.setUsuario(this.sessao);
//				
//				modelo.addRow(new Object[] {
//						agenda.getId(),
//						agenda.getNome(),
//						
//						
//				});
//			}
//			
//		} catch (IOException | SQLException e) {
//			JOptionPane.showMessageDialog(null, e.getMessage());
//		}
//		
//		
//		 
//		
//	}
	
	public void buscarCompromissos() {

		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		System.out.println("funcionou");
		modelo.fireTableDataChanged();
		
		modelo.setRowCount(0);
		
		
		List<Compromisso> compromissoLista;
		try {
			String nomeAgendaSelecionada = (String) comboBoxAgendas.getSelectedItem();
			
			compromissoLista = this.compromissoService.buscarCompromissos(retornarAgendaSelecionada().getIdAgenda());
			
			for(Compromisso compromisso : compromissoLista) {
				System.out.println(compromisso.getTitulo());
				
				modelo.addRow(new Object[] {
						
						compromisso.getIdCompromisso(),
						compromisso.getTitulo(),
						compromisso.getDataInicio(),
						
						
				});
			}
			
		} catch (IOException | SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	
	private Compromisso retornarCompromisso() {
		List<Compromisso> agendaCompromisso;
		try {
			
			if(table.getSelectedRow()==-1)return null;
			int idCompromissoSelecionado = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
			agendaCompromisso = this.compromissoService.buscarCompromissos(retornarAgendaSelecionada().getIdAgenda());
			
			for(Compromisso comp : agendaCompromisso) {
			if(comp.getIdCompromisso()==idCompromissoSelecionado) {
			
				return comp;
			}
			}
			
		} catch (IOException | SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return null;
		
	}
	
	
	private void configurarCompromissos() {
		
	
		
		Agenda agendaSelecionada = retornarAgendaSelecionada();
		
		if(agendaSelecionada !=null) {
			new CompromissoWindow(agendaSelecionada).setVisible(true);
			dispose();
			
		}else {
			JOptionPane.showMessageDialog(null,"Nenhuma agenda selecionada","Editar compromissos",  JOptionPane.WARNING_MESSAGE );
		}
		
	
	}
	
	
	private Agenda retornarAgendaSelecionada() {
		List<Agenda> agendaLista = new ArrayList<>();
		
		try {
			
			String nomeAgendaSelecionada = (String) comboBoxAgendas.getSelectedItem();
			
			agendaLista = this.agendaService.buscarAgendas(Sessao.getUsuario().getIdUsuario());
			
			for(Agenda agenda : agendaLista) {
			if(agenda.getNome().equals(nomeAgendaSelecionada)) {
				return agenda;
			}
			}
			
		} catch (IOException | SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return null;
		
	}
}
