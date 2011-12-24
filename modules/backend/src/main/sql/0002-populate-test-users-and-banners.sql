use `topfirst`;
/* email='1@1.com' password='111' sig='408778833bb3a85bc6e418f96b8b22d9' */
insert into `users` (user_id, email, first_name, last_name, password_signature, disabled)
  values(1, '1@1.com', 'test_1', 'user_1', '408778833bb3a85bc6e418f96b8b22d9', 0);

/*email='2@2.com' password='222' sig='7a19947f4acbcd21c61bb033d261022'*/
insert into `users` (user_id, email, first_name, last_name, password_signature, disabled)
  values(2, '2@2.com', 'test_2', 'user_2', '7a19947f4acbcd21c61bb033d261022', 0);

insert into `users` (user_id, email, first_name, last_name, password_signature, disabled)
  values(3, 'test_user_3@host.com', 'test_3', 'user_3', 'zxcvbnasdfghqwer', 0);

insert into `banners` (banner_id, user_id, title, intro, comments, image_path, creation_date, rank)
  values(1, 1, 'QWE', 'QWERTY', 'QWERTY-ASDFGH-ZXCVBNM', 'resources/img/banner-place-holder.png', now(), 123);

insert into `banners` (banner_id, user_id, title, intro, comments, image_path, creation_date, rank)
  values(2, 2, 'ASD', 'ASDFGH', 'ASDFGH-ZXCVBN-QWERTYU', 'resources/img/banner-place-holder.png', now(), 234);

insert into `banners` (banner_id, user_id, title, intro, comments, image_path, creation_date, rank)
  values(3, 3, 'ZXC', 'QAZWSX', 'EDCRFV-TGBYHN-UJMIKOL', 'resources/img/banner-place-holder.png', now(), 345);

--select * from users;
--select * from banners;
