- 问题：centos7 上面安装ftp的服务器， 方法基本在网上都有，但是仍然会遇到坑， 尤其是返回了421的错误
- 解决方案：安装vsftp

### 安装步骤：vsftp的版本 3.0.2-22
1. 使用yum来安装
> yum install vsftpd

2. 修改配置文件，默认是/etc/vsftpd/vsftpd.conf 以下注释项取出注释
> chroot_local_user=YES
> ascii_upload_enable=YES
> ascii_download_enable=YES

3. 修改配置文件, 增加一行
> allow_writeable_chroot=YES

4. 新增ftp的用户，并配置不允许ssh
> useradd -d /home/ftp/ui -s /bin/bash username
> usermod -s /usr/sbin/nologin username

5. 配置坑爹的setbool， 如果不配置客户端可能会返回500 OOPS: chroot Login failed. 421 Service not available的错误。 
> setsebool -P ftpd_full_access on
> setsebool -P tftp_home_dir on