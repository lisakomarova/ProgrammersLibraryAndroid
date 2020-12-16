# SharedBooks
В рамках курсового проекта было разработано мобильное приложение, предоставляющее пользователю
- возможность регистрации (реализовано с помощью FirebaseAuth);
- списки книг, читателей, выдач;
- возможность создания и редактирования книг и читателей;
- сортировка книг по статусу;
- сортировка читателей по факту наличия книг на руках (те, у кого есть долг, и те, у кого его нет);
- поиск книг;
- возможность выдачи книг читателям;
- возможность отправки e-mail определенным пользователяи;
- учет возврата книг для добавленных читателей;

Для реализации безопасности было использовано:
1. Шифрование базы данных с помощью SQLCipher
- первый шаг:  добавление зависимостей в build.gradle apk
```jaml
    implementation 'net.zetetic:android-database-sqlcipher:4.3.0@aar'
    implementation "androidx.sqlite:sqlite:2.0.1"
```
- второй шаг: использование следующих импортов при создании файла для работы с бд
```java
    import net.sqlcipher.database.SQLiteDatabase;
    import net.sqlcipher.database.SQLiteOpenHelper;
```
- третий шаг: загрузка библиотек stlport_shared , sqlcipher_android и database_sqlcipher, а также файл icudt46l.dat в конструкторе класса для работы с бд
```java
   SQLiteDatabase.loadLibs(context);
```
- четвертый шаг: создание переменной, хранящей пароль, и методов доступа к бд
```java
   private static final String password = "yourpassword";
    public SQLiteDatabase getReadableDatabase() {
        return (super.getReadableDatabase(password));
    }

    public SQLiteDatabase getWritableDatabase() {
        return (super.getWritableDatabase(password));
    }
```

2. Невозможность создания скриншота с помощью установки флагов объекта Window
```java
getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
```
3. Биометерическая аутентификация с помощью fingerprint api
Была создана дополнительная активность, в которой содержится кнопка для запуска процесса аутентификации (функция auth). Данная активность вызывается при входе в приложение.   
```java
 public void auth() {

        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this).
                setTitle("Подтвердите свою личность").
                setSubtitle("Используйте свои биометрические данные").
                setNegativeButton("cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).build();
        biometricPrompt.authenticate(new CancellationSignal(), getMainExecutor(), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                finish();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(ShieldActivity.this, errString, Toast.LENGTH_LONG).show();

            }
        });
    }
```
4. Обфускация кода с помощью ProGuard
Для этого были выполнены следующие шаги:
- В файле app / build.gradle установлено для minifyEnabled значение true
- Использованы правила Proguard по умолчанию для Android и созданы свои.
```jaml
buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
``` 
- Отредактирован файл proguard-rules.pro. Сторонние библиотеки предоставляют информацию о том, как скрыть проект, в который они включены, например [SQLCipher](https://gist.github.com/developernotes/3040592)

## Руководство пользователя
При первом запуске приложения откроется активность с полями для ввода электронной почты и пароля, а также двумя кнопками: sign up (зарегистрироваться) и sign in (войти).
При заполнении необходимой информации и нажатии кнопки sign up, пользователь должен быть зарегистрирован в системе либо необходимо информировать его о том, что такой пользователь уже существует, в случае ввода электронной почты, ранее зарегистрированной, и о том, что электронная почта и пароль не валидные, в случае ввода данных не соответствующих требованиям.
При заполнении необходимой информации и нажатии кнопки нажатии sign in, пользователь должен попасть на главную активность либо необходимо информировать его о том, что введенные логин или пароль неправильные.

![alt text](https://sun9-35.userapi.com/W1ITtA5975D7t3y2yY8M8UFhDs23g-xncelm1Q/pnWD13gSA0Q.jpg?type=album)

После того, как вы войдете или зарегистрируетесь как обычный пользователь, вам откроется страница с биометрической аутентификацией, после успешной аутентификации откроется страница со списком книг

![alt text](https://sun9-56.userapi.com/_nA4VPJKKz8c5w_DmEDx3FDqcPhefaB2G9fcJA/rvu37M0OgCI.jpg?type=album) ![alt text](https://sun9-52.userapi.com/1xOtEh4fZHnyW2HQYVRPXwWQq9gj81DLG7OjOg/uIixgBhw5M0.jpg?type=album) 

При долгом нажатии на элемент списка откроется контекстное меню

![alt text](https://sun9-10.userapi.com/DMB9JdSgfC86gqXvwuR9ZBiyiNZgGXR6RgGswQ/e866lIJJl-E.jpg?type=album)

При нажатии на кнопку добавить откроется страница добавления книги 

![alt text](https://sun9-31.userapi.com/-CC1lJbY2E4H9FVBuXrp8MHhgYo14IAtQurwEw/uUGrb7lWAVw.jpg?type=album)

После нажатия на Readers откроется фрагмент с со списком читателей. На экране показаны кружки, которые являются индикатором, есть ли у читателя долг.

![alt text](https://sun9-6.userapi.com/ICCmB2WK5ZQjugxmhAK0Yy-NLuDTsckNgDhuCg/seB8RghdVtA.jpg?type=album)

При добавлении читателя поле адреса электронной почты почты должно быть уникальным.

![alt text](https://sun9-59.userapi.com/q__VLMd_uHNcHvlVjy6bhbW8VSozo4Pd6VmIDg/_IvDG3QasGI.jpg?type=album)

При нажатии на вкладку Loans, появится список выдач книг

![alt text](https://sun9-35.userapi.com/Og9RyOQ4OBm5hsCTGayRkrPnGsMuqceIZJl5Ww/jSHxSAE1sAU.jpg?type=album)
