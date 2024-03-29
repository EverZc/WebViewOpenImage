package me.everzc.webviewimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {

    private String content = "<div  class='article-content' >" +
            "重庆市是我国四大直辖市之一，抗战时期曾为国民政府陪都，位于我国长江上游。重庆有“山城”与“雾都”之称，它四面环山，依山而建，又因地处盆地边缘，两江汇合处，常年雾气朦胧。重庆还被称为“小香港”，是出了名的的不夜城，它的夜景、美食、美女是其三大名片。其实除了这些，重庆的美景也是很值得一游的，最近的抖音更是带红了重庆的很多景点。" +
            "<br><br>洪崖洞<br><img width=\"1238.0\" height=\"425.0\"" +
            " src=\"https://luzhouapp.oss-cn-shenzhen.aliyuncs.com/15562934317169021B2Bw=638_h=425.0B2B.jpeg\" >" +
            "洪崖洞，原名洪崖门，是古重庆城门之一，历来为军事要塞，也是重庆城的一大胜景。洪崖洞现为国家4A级旅游景区，由纸盐河酒吧街、天成巷巴渝风情街、盛宴美食街和异域风情城市阳台四部分组成。它共有11层，以最具巴渝传统建筑特色的“吊脚楼”风貌为主体，依山沿崖而建，顶层可观江景。<br><br>洪崖洞是重庆历史文化的见证和重庆城市精神的象征，也是重庆网红景点的榜首打卡胜地。洪崖洞是欣赏山城夜景的好地方。夜晚时候灯火通明，灯光从晚上6点开灯，10点熄灯。吊脚楼被灯光环绕，像镀了层金，远远望去，如同宫崎骏的电影《千与千寻》中的不可思议之街，也因此成为网红打卡地。" +
            "<br><br>磁器口古镇<br><img width=\"604.0\" height=\"403.0\" " +
            "src=\"https://luzhouapp.oss-cn-shenzhen.aliyuncs.com/15562934434821699B2Bw=604_h=403.0B2B.jpeg\" >磁器口古镇位于重庆市沙坪坝区嘉陵江畔，因出产瓷器而得名。它始建于宋代，曾经“白日里千人拱手，入夜后万盏明灯”繁盛一时，嘉陵江边重要的水陆码头。现为国家4A级旅游景区，重庆“新巴渝十二景”。“一条石板路，千年磁器口”。磁器口古镇是重庆古城的缩影和象征，被赞誉为“小重庆”。<br><br>磁器口古镇已有1800年历史，素有巴渝第一古镇之称，保存了较为完整的古建。它有12条街巷，街道两旁大多是明清风格的建筑，脚下都是青石板路。走一下其中的小巷，品尝下当地的特色小吃，欣赏下当地的民间艺术，找个茶馆坐坐，一路吃吃逛逛，感受“老重庆”的风土人情。" +
            "<br><br>解放碑步行街<br>" +
            "<img width=\"638.0\" height=\"426.0\"src=\"https://luzhouapp.oss-cn-shenzhen.aliyuncs.com/15562934558998310B2Bw=638_h=426.0B2B.jpeg\" >" +
            "解放碑步行街位于重庆市渝中区，是重庆甚至是中国西部地区最繁华的商业中心地带。步行街以解放碑为中心，包括周边的民权路、民族路、邹容路和八一路和五一路等。这里是重庆时尚地标之一，有比较平民的重百大楼和太平洋百货，能吃到几乎所有的重庆美食，也是来看重庆美女的好地方。晚上是逛解放碑区域最好的时候，夜晚华灯初上，解放碑区域是人流最多的地方，琳琅满目的商品，现代化的高楼大厦，映衬着各种灯光秀，非常漂亮。<br><br>步行街上的解放碑，全称为“抗战胜利纪功碑”，是抗战胜利和重庆解放的历史见证，是中国唯一一座纪念中华民族抗日战争胜利的纪念碑。抗日战争全面爆发后，国民政府迁都重庆。为了动员民众抗日救国，建成了一座碑形建筑，名为“精神保垒”（意指坚决抗战的精神）。抗日战争胜利后，重庆市在原“精神保垒”的旧址上，建立“抗战胜利纪功碑”，以纪念抗战胜利。" +
            "<br><br>大足石刻<br><img width=\"569.0\" height=\"428.0\" " +
            "src=\"https://luzhouapp.oss-cn-shenzhen.aliyuncs.com/15562934726323516B2Bw=569_h=428.0B2B.jpeg\" >大足石刻位于重庆市大足县境内，是世界文化遗产、世界八大石窟之一，有“东方艺术明珠”之称。大足石刻为唐、五代、宋时所凿造，明、清两代亦续有开凿，是世界石窟艺术的最后丰碑。大足石刻是石刻群的统称，包含宝顶山、北山、南山、石篆山、石门山5处，一般人们所提及的大足石刻大部分都指的是宝顶山石刻。中国有四大石窟之说，虽然大足石刻并未列入其中，但以其宗教内涵和艺术价值来看，它是完全不输四大石窟的，甚至保存状况还比四大要更好。<br><br>大足石刻代表了代表了唐宋时期石刻艺术的最高水平，是中国晚期石窟造像艺术的典范。大足石刻群有石刻造像70多处，总计10万多躯。大足石刻保存得很完整，因此可以看到很多表述完整的故事和链条。它以佛教题材为主，每个雕刻都有自己的故事，更特别的是它还是彩色的，显得十分逼真，甚至活灵活现。特别是石洞中的佛像，不说表情神态，就连衣服的流苏仿佛都是一阵风就可以吹起来。<br><br>重庆是国家历史文化名城，是巴渝文化发祥地，抗战时期又是国民政府陪都。它特殊的地理环境与人文历史使其与众不同。一生中一定要去重庆旅游啊，看看这座特别的、有无数故事的山城。你去过重庆旅游吗？你喜欢重庆的哪里呢？说一下你的看法吧。" +
            "<br><br></div>";

    Html5WebView mWebView;
    private String[] imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageUrls = StringUtils.returnImageUrlsFromHtml(content);
        mWebView = findViewById(R.id.web_view);
        //为webview添加js 用于点击图片进入查看图片详情的activity
        mWebView.addJavascriptInterface(new MJavascriptInterface(MainActivity.this, imageUrls),
                "imagelistener");
        //第一步：首先获得html的标签内容 并转换。
        String text = content;//网页获取的html文本
        Document mDocument = Jsoup.parse(text);
        //第二步：获取img标签 <img width= "638.0" height= "426.0" src="https://luzhouapp.oss-cn-shenzhen.aliyuncs.com/15562934558998310B2Bw=638_h=426.0B2B.jpeg" >
        Elements imageSrc = mDocument.select("img[src]");
        //第三步：遍历当前html所有的img标签，获取元素 计算宽高并配置懒加载等。
        for (Element element : imageSrc) {
            element.addClass("lazy");
            String imgUrl = element.attr("src"); //获取当前图片的地址
            double mWidth = Double.parseDouble(element.attr("width")); //获取当前标签图片的宽,并转换为double类型
            double mHeight = Double.parseDouble(element.attr("height"));//获取当前标签图片的高，并将高转换为double类型
            double widthDp = DensityUtil.           //将宽高转换成 dp
                    px2dp(MainActivity.this, (float) mWidth);
            double heightDp = DensityUtil.
                    px2dp(MainActivity.this, (float) mHeight);
            double n = widthDp / heightDp;   //宽高比  宽度 / 高度
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            double widthScreen = wm.getDefaultDisplay().getWidth();
            int wScreenDP = DensityUtil.   //当前屏幕的宽度dp
                    px2dp(MainActivity.this, (float) widthScreen) - 35; //数字变低实际越高 变高越低
            LogUtils.e("screen==== " + wScreenDP);
            double calculateHeight = wScreenDP / n;
            element.attr("src", "file:///android_asset/ic_picture_default_four.png");//放置占为图
            element.attr("width", wScreenDP + "")
                    .attr("height", calculateHeight + "");
            element.attr("data-original", imgUrl);//data-original是图片延迟加载代码,目的就是为了进行懒加载，可以防治占为图
        }
        text = mDocument.toString();
        mWebView.setupBody(text);
    }
}
