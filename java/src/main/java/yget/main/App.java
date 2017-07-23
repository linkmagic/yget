package yget.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import yget.constants.Constants;
import yget.impls.HttpDownloadUtility;
import yget.impls.URLFileParseUtility;;

public class App
{
	public static void main(String[] args)
	{
		String saveDir = "./";
		ApplicationContext appCtx;

		if (args.length == 0)
		{
			System.out.println(Constants.aboutHelp);
			return;
		}
		else if (args.length == 1)
		{
			appCtx = new ClassPathXmlApplicationContext("context.xml");
			Object httpDownloaderBean = appCtx.getBean("httpDownloaderBean");
			if (httpDownloaderBean instanceof HttpDownloadUtility)
			{
				HttpDownloadUtility httpDownloadUtility = (HttpDownloadUtility) httpDownloaderBean;
				String fileUrl = args[0];
				System.out.println("START");
				if (httpDownloadUtility.downloadFile(fileUrl, saveDir))
				{
					System.out.println("OK: " + fileUrl);
				}
				else
				{
					System.out.println("Error: " + fileUrl);
				}
				System.out.println("FINISH");
			}
			else
			{
				System.out.println("objHttpDownloader instanceof HttpDownloadUtility => FALSE");
				return;
			}
		}
		else if (args.length >= 2)
		{
			if (args[0].equals("-f"))
			{
				appCtx = new ClassPathXmlApplicationContext("context.xml");
				Object fileParserBean = appCtx.getBean("urlFileParserBean");
				if (fileParserBean instanceof URLFileParseUtility)
				{
					URLFileParseUtility urlFileParserUtility = (URLFileParseUtility) fileParserBean;
					String[] urlList = urlFileParserUtility.parse(args[1]);
					if (urlList != null && urlList.length > 0)
					{
						Object httpDownloaderBean = appCtx.getBean("httpDownloaderBean");
						if (httpDownloaderBean instanceof HttpDownloadUtility)
						{
							int countOk = 0;
							int countError = 0;
							HttpDownloadUtility httpDownloadUtility = (HttpDownloadUtility) httpDownloaderBean;
							System.out.println("START");
							for (String urlItem : urlList)
							{
								if (httpDownloadUtility.downloadFile(urlItem, saveDir))
								{
									countOk++;
									System.out.println("OK: " + urlItem);
								}
								else
								{
									countError++;
									System.out.println("Error: " + urlItem);
								}
							}
							System.out.println("FINISH\n" + "Downloaded: " + countOk + "\nErrors: " + countError);
						}
						else
						{
							// System.out.println("WARNING: objHttpDownloader instanceof HttpDownloadUtility => FALSE");
							return;
						}
					}
					else
					{
						// System.out.println("WARNING: urlList != null && urlList.length > 0 => FALSE");
						return;
					}
				}
				else
				{
					// System.out.println("WARNING: objFileParser instanceof URLFileParseUtility => FALSE");
					return;
				}
			}
		}
	}

}
