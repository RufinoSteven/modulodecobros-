package utils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import dbConnections.DBConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SftpUploader {
    private static final Logger log = LogManager.getLogger(SftpUploader.class);
    private static Session session = null;
    private static ChannelSftp sftpChannel = null;

    private static boolean connectSftp() {
        if (session != null && session.isConnected() && sftpChannel != null && sftpChannel.isConnected()) {
            return true;
        }
        try {
            DBConfig config = DBConfig.getInstance();
            log.info("[SFTP] server   = {}", config.getServer());
            log.info("[SFTP] port     = {}", config.getPort());
            log.info("[SFTP] username = {}", config.getUsername());
            JSch jsch = new JSch();
            session = jsch.getSession(
                    config.getUsername(),
                    config.getServer(),
                    config.getPort()
            );
            session.setPassword(config.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            return true;
        } catch (Exception e) {
            log.error("Error al conectar con el servidor SFTP: {}", e.getMessage());
            return false;
        }
    }

    public static boolean uploadFile(String remoteFolderPath, String localFilePath) {
        if (connectSftp()) {
            File localFile = new File(localFilePath);
            if (localFile.exists() && localFile.isFile()) {
                try (FileInputStream fis = new FileInputStream(localFilePath)) {
                    sftpChannel.cd(remoteFolderPath);
                    sftpChannel.put(fis, localFile.getName());
                    log.info("El archivo se subió exitosamente.");
                    return true;
                } catch (IOException | SftpException e) {
                    log.error("Error durante la subida del archivo: {}", e.getMessage());
                } finally {
                    disconnectSftp();
                }
            } else {
                log.warn("El archivo local no existe o no es un archivo válido.");
            }
        } else {
            log.error("No se pudo conectar al servidor SFTP.");
        }
        return false;
    }

    // Método para desconectar la sesión y el canal SFTP cuando ya no se necesiten
    public static void disconnectSftp() {
        if (sftpChannel != null && sftpChannel.isConnected()) {
            sftpChannel.exit();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

}

