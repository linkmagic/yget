package yget.impls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import yget.interfaces.HttpDownloader;

public class HttpDownloadUtility implements HttpDownloader, InitializingBean, DisposableBean
{
	private final int BUFFER_SIZE = 4096;

	public boolean downloadFile(String fileURL, String saveDir)
	{
		try 
		{
			URL url = new URL(fileURL);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();

			// always check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK)
			{
				String fileName = "";
				String disposition = httpConn.getHeaderField("Content-Disposition");
				//String contentType = httpConn.getContentType();
				//int contentLength = httpConn.getContentLength();

				if (disposition != null)
				{
					// extracts file name from header field
					int index = disposition.indexOf("filename=");
					if (index > 0)
					{
						fileName = disposition.substring(index + 10, disposition.length() - 1);
					}
				} 
				else
				{
					// extracts file name from URL
					fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
				}

				// opens input stream from the HTTP connection
				InputStream inputStream = httpConn.getInputStream();
				String saveFilePath = saveDir + File.separator + fileName;

				// opens an output stream to save into file
				FileOutputStream outputStream = new FileOutputStream(saveFilePath);

				int bytesRead = -1;
				byte[] buffer = new byte[BUFFER_SIZE];
				while ((bytesRead = inputStream.read(buffer)) != -1)
				{
					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.close();
				inputStream.close();
			}
			else
			{
				return false;
			}
			httpConn.disconnect();
		}
		catch (IOException e)
		{
			return false;
		}
		
		return true;
	}

	public void destroy() throws Exception {
		
	}

	public void afterPropertiesSet() throws Exception {
		
	}

}
