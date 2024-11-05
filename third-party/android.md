# 数据转换
## TextToSpeech(TTS)

1. 配置manifest，选择TTS引擎(示例为原生引擎`android.intent.action.TTS_SERVICE`)
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.dongle.demo">

    <!-- application -->

    <queries> 
        <intent>
            <!-- 若未配置，则报警告：W/TextToSpeech: speak failed: not bound to TTS engine -->
            <action android:name="android.intent.action.TTS_SERVICE"/>
        </intent>
    </queries>
</manifest>
```
2. 实例化TTS
```kotlin
// 建议单例模式
var tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
    if (status == TextToSpeech.SUCCESS) {
        tts.setLanguage(Locale.CHINA)
        tts.setPitch(1.5f);
        tts.setSpeechRate(0.5f);
    }
})
```
```java
final TextToSpeech textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            textToSpeech.setLanguage(Locale.CHINESE);
            textToSpeech.setPitch(1.5f);
            textToSpeech.setSpeechRate(0.5f);
        }
    }
});
```
3. tts配置：所有设置都有返回值(int)，**大于或等于0代表成功，小于0代表失败**
   * `setLanguage` 设置语言或地区
   * `setPitch` 设置音调（**大于1.0为女声，小于1.0为男声**）
   * `setSpeechRate` 设置语速
4. 文字转语音适用
```java
    tts.speak("你好", TextToSpeech.QUEUE_FLUSH, null, null)
```
5. 注意
   * 更换其它**引擎**需要先下载引擎
   * 指定语言时需确保**语言包**已下载

# 切换
## Activity


## Fragment
### 通过RadioGroup切换Fragment
* 创建多个Fragment并关联参数
    ```xml
    <!-- 两个Fragment相同 -->
    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".FirstFragment">

        <!-- TODO: Update blank fragment layout -->
        <TextView
            android:id="@+id/tv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="@string/hello_blank_fragment" />

    </FrameLayout>
    ```
    ```kotlin
    private const val ARG_PARAM1 = "param1"
    class FirstFragment : Fragment() {
        private var param1: String? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                param1 = it.getString(ARG_PARAM1)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view:View = inflater.inflate(R.layout.fragment_first, container, false)
            val tv:TextView = view.findViewById(R.id.tv)
            tv.text=param1
            return view
        }

        companion object {
            @JvmStatic
            fun newInstance(param1: String) =  // newInstance方法用于创建Fragment实例
                FirstFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
        }
    }
    ```
* 在Activity中绑定Fragment
  * 布局中加入Fragment:`FrameLayout`
    ```xml
        <FrameLayout
            android:id="@+id/fg"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
        
        <RadioGroup
            android:id="@+id/radioGroup"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:orientation="horizontal"
            app:layout_constraintBottom_toBottomOf="parent">
            <RadioButton
                android:id="@+id/rbFirst"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="1"
                android:button="@null"
                android:gravity="center"
                android:text="首页"/>
            <RadioButton
                android:id="@+id/rbSecond"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="1"
                android:button="@null"
                android:gravity="center"
                android:text="第二页"/>
        </RadioGroup>
    ```
  * 操作Fragment切换:`Fragment.OnCheckedChangeListener`
    ```kotlin
    class MainActivity : AppCompatActivity() {
        private lateinit var fg:Fragment
        private lateinit var rg:RadioGroup
        
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            initView()
            intiFragment()

            rg.setOnCheckedChangeListener { _, checkedId -> // RadioGroup.OnCheckedChangeListener
                when (checkedId) {
                    R.id.rbFirst -> switchFragment(FirstFragment.newInstance("首页"))
                    R.id.rbSecond -> switchFragment(SecondFragment.newInstance("第二页"))
                }
            }
        }

        private fun initView(){
            rg = findViewById(R.id.radioGroup)
        }

        private fun intiFragment() {
            fg = FirstFragment.newInstance("首页");
            supportFragmentManager.beginTransaction().add(R.id.fg,fg).commit() //默认显示Fragment，主要便于切换时非空
        }

        private fun switchFragment(instance: Fragment) {
            if (fg != instance){
                if(!instance.isAdded){
                    supportFragmentManager.beginTransaction().hide(fg).add(R.id.fg,instance).commit()
                }else{
                    supportFragmentManager.beginTransaction().hide(fg).show(instance).commit()
                }
                fg = instance
            }
        }
    }
    ```

### 通过ViewPager切换Fragment
* 绑定ViewPager:`androidx.viewpager.widget.ViewPager`
```xml
<androidx.viewpager.widget.ViewPager
            android:id="@+id/vp"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
```
* 创建viewpager适配器`FragmentPagerAdapter`
```xml
class MyViewPager(fm:FragmentManager, private val mFragments:List<Fragment>): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragments.get(position)
    }
}
```
* 操作ViewPager切换:`ViewPager.OnPageChangeListener`
```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var rg:RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vp:ViewPager = findViewById(R.id.vp) // viewpager
        val first:RadioButton = findViewById(R.id.rbFirst)
        val second:RadioButton = findViewById(R.id.rbSecond)
        // fragment集
        val mFragmentList: MutableList<Fragment> = ArrayList()
        mFragmentList.add(FirstFragment.newInstance("首页"))
        mFragmentList.add(SecondFragment.newInstance("第二页"))
        // 绑定viewpager适配器
        vp.adapter = MyViewPager(supportFragmentManager, mFragmentList)
        // 监听viewpager切换
        vp.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int,positionOffset: Float,positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 0) {first.isChecked=true} 
                else if (position == 1) {second.isChecked=true}
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        // 由radiogroup切换viewpager
        rg = findViewById(R.id.radioGroup)
        rg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbFirst -> vp.currentItem = 0
                R.id.rbSecond -> vp.currentItem = 1
            }
        }
    }
}
```