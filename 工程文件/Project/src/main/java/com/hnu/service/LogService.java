package com.hnu.service;

import com.hnu.Mapper.LogMapper;
import com.hnu.entity.Log;
import com.hnu.util.SqlSessionUtil;
import jakarta.validation.constraints.Null;
import org.apache.ibatis.session.SqlSession;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LogService {

    private Path logDir = Paths.get("C:\\UAVSystem\\logs");

    public List<Log> findAllLog() {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        List<Log> logs = logMapper.findAllLog();
        session.close();
        return logs;
    }

    public List<Log> findLogByUId(int userId) {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        List<Log> logs = logMapper.findLogByUId(userId);
        session.close();
        return logs;
    }

    public Log findLogByLID(int logID) {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        Log log = logMapper.findLogByLID(logID);
        session.close();
        return log;
    }

    public List<Log> findLogByLevel(String level) {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        List<Log> logs = logMapper.findLogByLevel(level);
        session.close();
        return logs;
    }

    public List<Log> findLogByResource(String resource) {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        List<Log> logs = logMapper.findLogByResource(resource);
        session.close();
        return logs;
    }

    public List<Log> findLogByMessage(String message) {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        List<Log> logs = logMapper.findLogByMessage(message);
        session.close();
        return logs;
    }

    public List<Log> findLogByDate(Date date) {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        List<Log> logs = logMapper.findLogByDate(date);
        session.close();
        return logs;
    }

    public int addLog(Log log) {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        int result = logMapper.addLog(log);
        session.commit();
        session.close();
        return result;
    }

    public int deleteLogByUId(int userId) {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        int result = logMapper.deleteLogByUId(userId);
        session.commit();
        session.close();
        return result;
    }

    public int deleteUserByLId(int logID) {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        int result = logMapper.deleteUserByLId(logID);
        session.commit();
        session.close();
        return result;
    }

    public void close() {
        // No operation needed as each method closes the session individually
    }

    public List<Log> searchLog(String searchInput, String searchOption) throws ParseException {
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Log> logs = new ArrayList<>();
        if (searchInput == null) {
            logs = logMapper.findAllLog();
        } else {
            if (searchOption.equals("time")) {
                logs = logMapper.findLogByDate(sdf.parse(searchInput));
            } else if (searchOption.equals("rank")) {
                logs = logMapper.findLogByLevel(searchInput);
            } else if (searchOption.equals("source")) {
                logs = logMapper.findLogByResource(searchInput);
            } else if (searchOption.equals("information")) {
                logs = logMapper.findLogByMessage(searchInput);
            }
        }
        session.close();
        return logs;
    }

    public int getPages(List<Log> logs) {
        int length = logs.size();
        if (length % 8 == 0) {
            return length / 8;
        } else {
            return length / 8 + 1;
        }
    }

    public void addLogToFile(int userid, String info, String level, String source) throws IOException {
        LocalDate date = LocalDate.now();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();

        String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String filename = dateString + "用户" + userid + ".log";
        Path logDir = Paths.get("C:\\UAVSystem\\logs");
        if (!Files.exists(logDir)) {
            Files.createDirectories(logDir);
        }
        Path logFile = logDir.resolve(filename);
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String logEntry = sdf.format(time) + " " + level + " " + source + " " + info + " " + userid + System.lineSeparator();
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将指定目录下的所有日志文件内容写入数据库，并删除日志文件
     *
     * @throws IOException    当读取文件或删除文件出现I/O异常时抛出
     * @throws ParseException 当解析日志文件中的日期字符串出现异常时抛出
     */
    public List<Log> importLogsFromFilesAndDelete() throws IOException, ParseException {
        if (!Files.exists(logDir)) {
            return Collections.emptyList();
        }

        List<Log> logsToInsert = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SqlSession session = SqlSessionUtil.openSession();
        LogMapper logMapper = session.getMapper(LogMapper.class);

        // 遍历目录下的所有日志文件
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logDir)) {
            for (Path logFile : stream) {
                try (BufferedReader reader = Files.newBufferedReader(logFile)) {
                    String line;
                    while ((line = ((BufferedReader) reader).readLine()) != null) {
                        String[] parts = line.split(" ");
                        if (parts.length >= 5) {
                            Log log = new Log();
                            log.setDate(sdf.parse(parts[0] + " " + parts[1]));
                            log.setLevel(parts[2]);
                            log.setResource(parts[3]);
                            log.setMessage(parts[4]);
                            if (parts.length > 5) {
                                log.setUserId(Integer.parseInt(parts[5]));
                            }
                            System.out.println(log.getUserId());
                            logsToInsert.add(log);
                        }
                    }
                }
                // 删除已经读取完的日志文件
                Files.delete(logFile);
            }
        }
        System.out.println(logsToInsert.get(0).getUserId());
        return logsToInsert;
    }
}