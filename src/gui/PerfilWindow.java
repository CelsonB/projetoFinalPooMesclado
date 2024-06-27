package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import entities.Compromisso;
import entities.Sessao;
import service.CompromissoService;
import service.NotificacaoThread;

public class PerfilWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblFotoPerfil;
	private JLabel lblNomeUsuario;
	private JLabel lblDataDeNascimento;
	private JLabel lblEmail;
	private JLabel lblNomeCompleto;
	private JButton btnAgendas;

	private CompromissoService compromissoService = new CompromissoService();
	private NotificacaoThread notificacao;
	private JLabel lblConvite;
	private JLabel lblBemVindo;

	public PerfilWindow() {
		initComponents();
		buscarConvites();
		preencherDados();
		iniciarNotificacao();
	}

	private void iniciarNotificacao() {
		this.notificacao = new NotificacaoThread();
		this.notificacao.start();
	}

	private void preencherDados() {
		try {
			lblNomeCompleto.setText("Nome: " + Sessao.getUsuario().getNomeCompleto());
			lblDataDeNascimento.setText("Data de nascimento: " + Sessao.getUsuario().getDataNascimento().toString());
			lblEmail.setText("Email: " + Sessao.getUsuario().getEmail());
			lblNomeUsuario.setText("Usuário: " + Sessao.getUsuario().getNomeUsuario());
			
			if (Sessao.getUsuario().getImagemPerfil() != null) {
				lblFotoPerfil.setIcon(converterBytesParaIcon(Sessao.getUsuario().getImagemPerfil()));
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao obter dados de usuário", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private ImageIcon converterBytesParaIcon(byte[] bytes) throws IOException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
			BufferedImage bufferedImage = ImageIO.read(bais);
			return new ImageIcon(bufferedImage);
		}
	}
	
	private void buscarConvites() {
		try {
			List<Compromisso> convites =  compromissoService.buscarConvites(Sessao.getUsuario().getIdUsuario());
			if (!convites.isEmpty()) {
				lblBemVindo.setVisible(false);
				lblConvite.setVisible(true);
			}
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao obter convites", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void editarUsuario() {
		this.dispose();
		new CadastrarUsuarioWindow().setVisible(true);
	}

	private void abrirAgenda() {
		this.dispose();
		new AgendaWindow().setVisible(true);
	}
	
	private void realizarLogout() {
		this.dispose();
		new LoginWindow().setVisible(true);
		Sessao.setUsuario(null);
	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 615, 273);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 35, 575, 155);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(panel);

		lblNomeCompleto = new JLabel();
		lblNomeCompleto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNomeCompleto.setText("Nome: <dynamic>");
		lblNomeCompleto.setBounds(171, 29, 283, 14);

		lblEmail = new JLabel();
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEmail.setBounds(171, 54, 283, 14);

		lblDataDeNascimento = new JLabel();
		lblDataDeNascimento.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDataDeNascimento.setBounds(171, 110, 283, 14);
		panel.setLayout(null);
		panel.add(lblNomeCompleto);
		panel.add(lblEmail);
		panel.add(lblDataDeNascimento);

		lblFotoPerfil = new JLabel("");
		lblFotoPerfil.setBounds(10, 10, 130, 130);
		panel.add(lblFotoPerfil);

		lblNomeUsuario = new JLabel("Usuário: <dynamic>");
		lblNomeUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNomeUsuario.setBounds(171, 82, 283, 14);
		panel.add(lblNomeUsuario);

		JButton btnAtualizarPerfil = new JButton("Editar perfil");
		btnAtualizarPerfil.setBounds(435, 117, 130, 23);
		panel.add(btnAtualizarPerfil);
		btnAtualizarPerfil.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAtualizarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarUsuario();
			}
		});

		btnAgendas = new JButton("Agendas");
		btnAgendas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirAgenda();
			}
		});
		btnAgendas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAgendas.setBounds(427, 201, 158, 23);
		contentPane.add(btnAgendas);

		JButton btnDeslogar = new JButton("Logout");
		btnDeslogar.setBounds(10, 201, 158, 23);
		contentPane.add(btnDeslogar);
		btnDeslogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizarLogout();
			}
		});
		btnDeslogar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblConvite = new JLabel("Você possui convites pendentes!");
		lblConvite.setForeground(Color.RED);
		lblConvite.setBounds(10, 10, 287, 14);
		contentPane.add(lblConvite);
		lblConvite.setHorizontalAlignment(SwingConstants.LEFT);
		lblConvite.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblBemVindo = new JLabel("Bem Vindo!");
		lblBemVindo.setHorizontalAlignment(SwingConstants.LEFT);
		lblBemVindo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBemVindo.setBounds(10, 10, 204, 14);
		contentPane.add(lblBemVindo);
		lblConvite.setVisible(false);
		
		setTitle("Home");
		setResizable(false);

		setLocationRelativeTo(null);
	}
}
