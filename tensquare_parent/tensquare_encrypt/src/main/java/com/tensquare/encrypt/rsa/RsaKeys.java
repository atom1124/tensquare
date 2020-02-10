package com.tensquare.encrypt.rsa;

/**
 * rsa加解密用的公钥和私钥
 * @author Administrator
 *
 */
public class RsaKeys {

	//生成秘钥对的方法可以参考这篇帖子
	//https://www.cnblogs.com/yucy/p/8962823.html

//	//服务器公钥
//	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Dw9nwjBmDD/Ca1QnRGy"
//											 + "GjtLbF4CX2EGGS7iqwPToV2UUtTDDemq69P8E+WJ4n5W7Iln3pgK+32y19B4oT5q"
//											 + "iUwXbbEaAXPPZFmT5svPH6XxiQgsiaeZtwQjY61qDga6UH2mYGp0GbrP3i9TjPNt"
//											 + "IeSwUSaH2YZfwNgFWqj+y/0jjl8DUsN2tIFVSNpNTZNQ/VX4Dln0Z5DBPK1mSskd"
//											 + "N6uPUj9Ga/IKnwUIv+wL1VWjLNlUjcEHsUE+YE2FN03VnWDJ/VHs7UdHha4d/nUH"
//											 + "rZrJsKkauqnwJsYbijQU+a0HubwXB7BYMlKovikwNpdMS3+lBzjS5KIu6mRv1GoE"
//											 + "vQIDAQAB";
//
//	//服务器私钥(经过pkcs8格式处理)
//	private static final String serverPrvKeyPkcs8 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDoPD2fCMGYMP8J"
//				 								 + "rVCdEbIaO0tsXgJfYQYZLuKrA9OhXZRS1MMN6arr0/wT5YniflbsiWfemAr7fbLX"
//				 								 + "0HihPmqJTBdtsRoBc89kWZPmy88fpfGJCCyJp5m3BCNjrWoOBrpQfaZganQZus/e"
//				 								 + "L1OM820h5LBRJofZhl/A2AVaqP7L/SOOXwNSw3a0gVVI2k1Nk1D9VfgOWfRnkME8"
//				 								 + "rWZKyR03q49SP0Zr8gqfBQi/7AvVVaMs2VSNwQexQT5gTYU3TdWdYMn9UeztR0eF"
//				 								 + "rh3+dQetmsmwqRq6qfAmxhuKNBT5rQe5vBcHsFgyUqi+KTA2l0xLf6UHONLkoi7q"
//				 								 + "ZG/UagS9AgMBAAECggEBANP72QvIBF8Vqld8+q7FLlu/cDN1BJlniReHsqQEFDOh"
//				 								 + "pfiN+ZZDix9FGz5WMiyqwlGbg1KuWqgBrzRMOTCGNt0oteIM3P4iZlblZZoww9nR"
//				 								 + "sc4xxeXJNQjYIC2mZ75x6bP7Xdl4ko3B9miLrqpksWNUypTopOysOc9f4FNHG326"
//				 								 + "0EMazVaXRCAIapTlcUpcwuRB1HT4N6iKL5Mzk3bzafLxfxbGCgTYiRQNeRyhXOnD"
//				 								 + "eJox64b5QkFjKn2G66B5RFZIQ+V+rOGsQElAMbW95jl0VoxUs6p5aNEe6jTgRzAT"
//				 								 + "kqM2v8As0GWi6yogQlsnR0WBn1ztggXTghQs2iDZ0YkCgYEA/LzC5Q8T15K2bM/N"
//				 								 + "K3ghIDBclB++Lw/xK1eONTXN+pBBqVQETtF3wxy6PiLV6PpJT/JIP27Q9VbtM9UF"
//				 								 + "3lepW6Z03VLqEVZo0fdVVyp8oHqv3I8Vo4JFPBDVxFiezygca/drtGMoce0wLWqu"
//				 								 + "bXlUmQlj+PTbXJMz4VTXuPl1cesCgYEA6zu5k1DsfPolcr3y7K9XpzkwBrT/L7LE"
//				 								 + "EiUGYIvgAkiIta2NDO/BIPdsq6OfkMdycAwkWFiGrJ7/VgU+hffIZwjZesr4HQuC"
//				 								 + "0APsqtUrk2yx+f33ZbrS39gcm/STDkVepeo1dsk2DMp7iCaxttYtMuqz3BNEwfRS"
//				 								 + "kIyKujP5kfcCgYEA1N2vUPm3/pNFLrR+26PcUp4o+2EY785/k7+0uMBOckFZ7GIl"
//				 								 + "FrV6J01k17zDaeyUHs+zZinRuTGzqzo6LSCsNdMnDtos5tleg6nLqRTRzuBGin/A"
//				 								 + "++xWn9aWFT+G0ne4KH9FqbLyd7IMJ9R4gR/1zseH+kFRGNGqmpi48MS61G0CgYBc"
//				 								 + "PBniwotH4cmHOSWkWohTAGBtcNDSghTRTIU4m//kxU4ddoRk+ylN5NZOYqTxXtLn"
//				 								 + "Tkt9/JAp5VoW/41peCOzCsxDkoxAzz+mkrNctKMWdjs+268Cy4Nd09475GU45khb"
//				 								 + "Y/88qV6xGz/evdVW7JniahbGByQhrMwm84R9yF1mNwKBgCIJZOFp9xV2997IY83S"
//				 								 + "habB/YSFbfZyojV+VFBRl4uc6OCpXdtSYzmsaRcMjN6Ikn7Mb9zgRHR8mPmtbVfj"
//				 								 + "B8W6V1H2KOPfn/LAM7Z0qw0MW4jimBhfhn4HY30AQ6GeImb2OqOuh3RBbeuuD+7m"
//				 								 + "LpFZC9zGggf9RK3PfqKeq30q";

	//服务器公钥
	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1Wj5CJIlqymPuhprlZ++\n" +
			"e0a7ZRVzpOHzq7TnzhCaCn5MclVAYM7rMUTTvPo6/IykIREeEN6fbT85Rq6JEJa7\n" +
			"2d+3rpLfsu1YWHoggqai4H/DrAGeBXxNEytpMA9joQUzLHu6Y/6wsLalKgVRZby0\n" +
			"haQxiRp9nKh4YSqHOamT82lnuBY1rKkGZgBTmk+WomHTje7cELTKVJNOFSnLcjk5\n" +
			"xyPwqeSBvMJdDPxeZtZUkVqkPRaoL9H5wADN5jimgQiigomBqPskg095olSnUxhU\n" +
			"HtclCbVpkg0s40ncSw/xYsNdFakxgGSquU8Dv3dIajAQfLtNwAKc5Rh3awvbuY9u\n" +
			"CQIDAQAB";

	//服务器私钥(经过pkcs8格式处理)
	private static final String serverPrvKeyPkcs8 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDVaPkIkiWrKY+6\n" +
			"GmuVn757RrtlFXOk4fOrtOfOEJoKfkxyVUBgzusxRNO8+jr8jKQhER4Q3p9tPzlG\n" +
			"rokQlrvZ37eukt+y7VhYeiCCpqLgf8OsAZ4FfE0TK2kwD2OhBTMse7pj/rCwtqUq\n" +
			"BVFlvLSFpDGJGn2cqHhhKoc5qZPzaWe4FjWsqQZmAFOaT5aiYdON7twQtMpUk04V\n" +
			"KctyOTnHI/Cp5IG8wl0M/F5m1lSRWqQ9Fqgv0fnAAM3mOKaBCKKCiYGo+ySDT3mi\n" +
			"VKdTGFQe1yUJtWmSDSzjSdxLD/Fiw10VqTGAZKq5TwO/d0hqMBB8u03AApzlGHdr\n" +
			"C9u5j24JAgMBAAECggEAU8uNBS2Fduix06wuFCkOPQ9v42gB6XsDF6fH4SSpp3ci\n" +
			"Az6wMZLtzIGu9iP251U92PIEsTEIvne1wKglj4rwJcNvNy/agEIrj0oc29AcYjQo\n" +
			"m7Bi4NqG1QEVo1ZXk1EcX9oRPNQzgp+kyJQf+ueWpmbrQF9TdSC9a8GsKRsJyegg\n" +
			"ooAq/4gKBObQRQetyN+2yZ3897J+CIjVnit8S3LRb2FenXWw6IYn1KAXDOOcf0Xb\n" +
			"1+mQRw1rTqglCzkyNWJluxLqG57/TvBcZ7wRmVCbge41WwaJ6o0gxbW0rZB1/iQy\n" +
			"0oA+mHnP1TTzNSQo5p3rmPz4fNDeLToOSS9SkCSVAQKBgQD0TKG81qyeD6YjUnNN\n" +
			"5veYl3GMpjvf0CG6PKzibVtK+Lg7wksoLRstGQmCxfAs36Iwa1QLGvJpTOBohugH\n" +
			"/86GeElt8w4Y7XBVqCXaAH4FAIwLCKQtO67TkBUb+CQgXKmEhijM/INp4NsY1q43\n" +
			"OQBwjL6PX3uMA4yUKnKtzQQSoQKBgQDfoZsJY00pmpRazSYF2qC4ejoxbMQMZqtv\n" +
			"dvGUVvB805X6Iv9TjNth8gT8qsVpS2iThFkrZWDKD0VTvYDQf87s3EXQu7b/K6qt\n" +
			"/UMX4ahRa8YPVlXcGLLRHUL0H0IXuv/NNmdcmV08cC/JlOCvHSlvTRFpWda7bI/X\n" +
			"j4g9azmKaQKBgQDQacfZeDMPLT6JB8ACh7ZW4Wwnl3GhAMilPzomeTDXBtT1dk3z\n" +
			"2ndCxAdPH1pEjgypzgaG6tg/aSxRQDYXDO693USXnjE6puWczbRtBNp9nffbOeYG\n" +
			"CNKe/l5j3A/F5AdiXanVJt9dpkyEJjG+PszXN9siKU0XJ7f4dzYae7qrwQKBgQCK\n" +
			"G+n9oEAVQ4v/j9a9osnDZGjwATYKWpXhntmPPSoETT/AXUDGPqV2FduSl3yXjKSn\n" +
			"BgNz04Y1A71S/CPDoEsQ6PCM+oEHkdY04cY/x3MF5cNJ0Y3xAafdkW66CVvt/+Te\n" +
			"vYyUGXsFSluY6MLiIuZmAHiSUZNV6LPk8pW3KLM+kQKBgB8mNhYwJh2HnZBuMvs/\n" +
			"VrkqeLzxY6AYkb4DFKiAFSLM5nzriiXLW16eLVsspdKiaUdriC9+vsERVUFDOEgX\n" +
			"3wjPEj8tVA6sZ9aq3i6iAClDSjZB14OKtVrsgqhsPXlFf3trJTdZgGukD5j4qkNQ\n" +
			"uI/E9yI0QwKW7SJv8n8SLBqo";

	public static String getServerPubKey() {
		return serverPubKey;
	}

	public static String getServerPrvKeyPkcs8() {
		return serverPrvKeyPkcs8;
	}
	
}
