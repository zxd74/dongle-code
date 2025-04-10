// 视频播放器核心类
class VideoPlayerCore {
    constructor(options) {
      // 初始化配置
      this.options = {
        autoplay: false,
        muted: false,
        loop: false,
        preload: 'auto',
        ...options
      };
      
      // 创建视频元素
      this.videoElement = document.createElement('video');
      this.setupVideoElement();
      
      // 初始化播放器状态
      this.isPlaying = false;
      this.currentSource = null;
      
      // 初始化插件系统
      this.plugins = new Map();
      
      // 事件系统
      this.eventListeners = {};
    }
  
    // 设置视频元素基本属性
    setupVideoElement() {
      this.videoElement.controls = false;
      this.videoElement.autoplay = this.options.autoplay;
      this.videoElement.muted = this.options.muted;
      this.videoElement.loop = this.options.loop;
      this.videoElement.preload = this.options.preload;
      
      // 添加性能优化属性
      this.videoElement.playsInline = true;
      this.videoElement.disablePictureInPicture = true;
    }
  
    // 挂载到DOM
    mount(container) {
      if (typeof container === 'string') {
        container = document.querySelector(container);
      }
      container.appendChild(this.videoElement);
      return this;
    }
  
    // 加载视频源
    async loadSource(source) {
      // 支持多种源类型
      if (typeof source === 'string') {
        this.currentSource = { src: source, type: this.getSourceType(source) };
      } else if (source && source.src) {
        this.currentSource = source;
      } else {
        throw new Error('Invalid video source');
      }
  
      // 触发源加载前事件
      this.emit('beforeLoad', this.currentSource);
  
      // 设置视频源
      this.videoElement.src = this.currentSource.src;
      
      if (this.currentSource.type) {
        this.videoElement.type = this.currentSource.type;
      }
  
      // 预加载元数据
      await new Promise((resolve, reject) => {
        const handleLoadedMetadata = () => {
          this.videoElement.removeEventListener('loadedmetadata', handleLoadedMetadata);
          resolve();
        };
        
        this.videoElement.addEventListener('loadedmetadata', handleLoadedMetadata);
        this.videoElement.addEventListener('error', (e) => {
          this.videoElement.removeEventListener('loadedmetadata', handleLoadedMetadata);
          reject(e);
        });
      });
  
      // 触发源加载完成事件
      this.emit('afterLoad', this.currentSource);
      
      return this;
    }
  
    // 自动检测源类型
    getSourceType(src) {
      const extension = src.split('.').pop().toLowerCase();
      const typeMap = {
        mp4: 'video/mp4',
        webm: 'video/webm',
        ogv: 'video/ogg',
        m3u8: 'application/x-mpegURL',
        mpd: 'application/dash+xml'
      };
      return typeMap[extension] || '';
    }
  
    // 播放控制
    play() {
      const playPromise = this.videoElement.play();
      
      if (playPromise !== undefined) {
        playPromise.then(() => {
          this.isPlaying = true;
          this.emit('play');
        }).catch(error => {
          this.emit('playError', error);
        });
      }
      
      return this;
    }
  
    pause() {
      this.videoElement.pause();
      this.isPlaying = false;
      this.emit('pause');
      return this;
    }
  
    togglePlay() {
      if (this.isPlaying) {
        this.pause();
      } else {
        this.play();
      }
      return this;
    }
  
    // 时间控制
    seek(time) {
      this.videoElement.currentTime = time;
      return this;
    }
  
    // 音量控制
    setVolume(volume) {
      this.videoElement.volume = Math.min(1, Math.max(0, volume));
      this.emit('volumeChange', this.videoElement.volume);
      return this;
    }
  
    mute() {
      this.videoElement.muted = true;
      this.emit('mute');
      return this;
    }
  
    unmute() {
      this.videoElement.muted = false;
      this.emit('unmute');
      return this;
    }
  
    // 全屏控制
    enterFullscreen() {
      if (this.videoElement.requestFullscreen) {
        this.videoElement.requestFullscreen();
      } else if (this.videoElement.webkitRequestFullscreen) {
        this.videoElement.webkitRequestFullscreen();
      } else if (this.videoElement.msRequestFullscreen) {
        this.videoElement.msRequestFullscreen();
      }
      return this;
    }
  
    exitFullscreen() {
      if (document.exitFullscreen) {
        document.exitFullscreen();
      } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen();
      } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
      }
      return this;
    }
  
    // 插件系统
    use(plugin, options) {
      if (this.plugins.has(plugin.name)) {
        console.warn(`Plugin ${plugin.name} already registered`);
        return this;
      }
      
      const pluginInstance = new plugin(this, options);
      this.plugins.set(plugin.name, pluginInstance);
      
      // 初始化插件
      if (typeof pluginInstance.init === 'function') {
        pluginInstance.init();
      }
      
      return this;
    }
  
    // 事件系统
    on(event, callback) {
      if (!this.eventListeners[event]) {
        this.eventListeners[event] = [];
      }
      this.eventListeners[event].push(callback);
      return this;
    }
  
    off(event, callback) {
      if (this.eventListeners[event]) {
        this.eventListeners[event] = this.eventListeners[event].filter(
          cb => cb !== callback
        );
      }
      return this;
    }
  
    emit(event, ...args) {
      if (this.eventListeners[event]) {
        this.eventListeners[event].forEach(callback => {
          callback.apply(this, args);
        });
      }
      return this;
    }
  
    // 销毁
    destroy() {
      // 暂停播放
      this.pause();
      
      // 移除视频元素
      if (this.videoElement.parentNode) {
        this.videoElement.parentNode.removeChild(this.videoElement);
      }
      
      // 清理插件
      this.plugins.forEach(plugin => {
        if (typeof plugin.destroy === 'function') {
          plugin.destroy();
        }
      });
      this.plugins.clear();
      
      // 清理事件监听
      this.eventListeners = {};
    }
}

// 控制栏插件
class ControlBarPlugin {
    constructor(player, options) {
      this.name = 'ControlBarPlugin';
      this.player = player;
      this.options = {
        showPlayPause: true,
        showProgress: true,
        showVolume: true,
        showFullscreen: true,
        ...options
      };
      
      this.controlsContainer = null;
    }
  
    init() {
      this.createControls();
      this.bindEvents();
    }
  
    createControls() {
      this.controlsContainer = document.createElement('div');
      this.controlsContainer.className = 'video-controls';
      
      if (this.options.showPlayPause) {
        const playPauseBtn = document.createElement('button');
        playPauseBtn.className = 'play-pause-btn';
        playPauseBtn.innerHTML = this.player.isPlaying ? '❚❚' : '▶';
        this.controlsContainer.appendChild(playPauseBtn);
      }
      
      if (this.options.showProgress) {
        const progressContainer = document.createElement('div');
        progressContainer.className = 'progress-container';
        
        const progressBar = document.createElement('div');
        progressBar.className = 'progress-bar';
        
        const bufferedBar = document.createElement('div');
        bufferedBar.className = 'buffered-bar';
        
        const timeDisplay = document.createElement('span');
        timeDisplay.className = 'time-display';
        timeDisplay.textContent = '00:00 / 00:00';
        
        progressContainer.appendChild(bufferedBar);
        progressContainer.appendChild(progressBar);
        progressContainer.appendChild(timeDisplay);
        this.controlsContainer.appendChild(progressContainer);
      }
      
      if (this.options.showVolume) {
        const volumeControl = document.createElement('div');
        volumeControl.className = 'volume-control';
        
        const volumeBtn = document.createElement('button');
        volumeBtn.className = 'volume-btn';
        volumeBtn.innerHTML = '🔊';
        
        const volumeSlider = document.createElement('input');
        volumeSlider.type = 'range';
        volumeSlider.min = '0';
        volumeSlider.max = '1';
        volumeSlider.step = '0.1';
        volumeSlider.value = this.player.videoElement.volume;
        
        volumeControl.appendChild(volumeBtn);
        volumeControl.appendChild(volumeSlider);
        this.controlsContainer.appendChild(volumeControl);
      }
      
      if (this.options.showFullscreen) {
        const fullscreenBtn = document.createElement('button');
        fullscreenBtn.className = 'fullscreen-btn';
        fullscreenBtn.innerHTML = '⛶';
        this.controlsContainer.appendChild(fullscreenBtn);
      }
      
      // 添加到播放器容器
      this.player.videoElement.parentNode.appendChild(this.controlsContainer);
    }
  
    bindEvents() {
      // 播放/暂停按钮事件
      const playPauseBtn = this.controlsContainer.querySelector('.play-pause-btn');
      if (playPauseBtn) {
        playPauseBtn.addEventListener('click', () => {
          this.player.togglePlay();
        });
      }
      
      // 播放器状态变化事件
      this.player.on('play', () => {
        if (playPauseBtn) playPauseBtn.innerHTML = '❚❚';
      });
      
      this.player.on('pause', () => {
        if (playPauseBtn) playPauseBtn.innerHTML = '▶';
      });
      
      // 进度条事件
      const progressBar = this.controlsContainer.querySelector('.progress-bar');
      const bufferedBar = this.controlsContainer.querySelector('.buffered-bar');
      const timeDisplay = this.controlsContainer.querySelector('.time-display');
      
      if (progressBar && timeDisplay) {
        // 更新进度条
        const updateProgress = () => {
          const percent = (this.player.videoElement.currentTime / this.player.videoElement.duration) * 100;
          progressBar.style.width = `${percent}%`;
          
          // 更新缓冲进度
          if (this.player.videoElement.buffered.length > 0) {
            const bufferedEnd = this.player.videoElement.buffered.end(this.player.videoElement.buffered.length - 1);
            const bufferedPercent = (bufferedEnd / this.player.videoElement.duration) * 100;
            bufferedBar.style.width = `${bufferedPercent}%`;
          }
          
          // 更新时间显示
          const formatTime = (time) => {
            const minutes = Math.floor(time / 60);
            const seconds = Math.floor(time % 60);
            return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
          };
          
          timeDisplay.textContent = `${formatTime(this.player.videoElement.currentTime)} / ${formatTime(this.player.videoElement.duration)}`;
        };
        
        this.player.videoElement.addEventListener('timeupdate', updateProgress);
        
        // 点击进度条跳转
        const progressContainer = this.controlsContainer.querySelector('.progress-container');
        progressContainer.addEventListener('click', (e) => {
          const rect = progressContainer.getBoundingClientRect();
          const pos = (e.clientX - rect.left) / rect.width;
          this.player.seek(pos * this.player.videoElement.duration);
        });
      }
      
      // 音量控制事件
      const volumeSlider = this.controlsContainer.querySelector('.volume-control input[type="range"]');
      const volumeBtn = this.controlsContainer.querySelector('.volume-btn');
      
      if (volumeSlider && volumeBtn) {
        volumeSlider.addEventListener('input', (e) => {
          this.player.setVolume(e.target.value);
        });
        
        volumeBtn.addEventListener('click', () => {
          if (this.player.videoElement.muted) {
            this.player.unmute();
            volumeSlider.value = this.player.videoElement.volume;
            volumeBtn.innerHTML = '🔊';
          } else {
            this.player.mute();
            volumeBtn.innerHTML = '🔇';
          }
        });
      }
      
      // 全屏按钮事件
      const fullscreenBtn = this.controlsContainer.querySelector('.fullscreen-btn');
      if (fullscreenBtn) {
        fullscreenBtn.addEventListener('click', () => {
          if (!document.fullscreenElement) {
            this.player.enterFullscreen();
          } else {
            this.player.exitFullscreen();
          }
        });
      }
    }
  
    destroy() {
      if (this.controlsContainer && this.controlsContainer.parentNode) {
        this.controlsContainer.parentNode.removeChild(this.controlsContainer);
      }
    }
}
