# StudyFlow — gerar APK de teste

## 1. Abrir no Android Studio

1. Extraia o ZIP.
2. Abra o Android Studio.
3. Escolha **Open** e selecione a pasta `StudyFlow`.
4. Aguarde o Gradle Sync.
5. Se o Android Studio pedir para instalar/atualizar o Android SDK, aceite o **Android SDK 35**.
6. Use **JDK 17** para o projeto.

Se a instalação do Android Studio não localizar uma distribuição do Gradle automaticamente, abra **Settings → Build Tools → Gradle**, selecione o JDK 17 e use Gradle 8.7.

## 2. Gerar o APK de teste

No Android Studio:

**Build → Generate App Bundles or APKs → Generate APKs**

Quando terminar, use:

`app/build/outputs/apk/debug/app-debug.apk`

Esse é o arquivo que pode ser enviado aos colegas para instalação no Android.

## 3. Instalar no celular

Envie `app-debug.apk` por cabo, Drive, WhatsApp ou outro meio.

No celular, se o Android bloquear a instalação, permita a instalação de aplicativos da fonte usada para abrir o APK.

## 4. O que funciona sem servidor

O StudyFlow usa Room/SQLite localmente. Portanto, o cadastro de matérias e avaliações e a persistência local funcionam em cada celular sem precisar deixar o backend Node.js ligado.

Cada celular terá seus próprios dados.

## 5. Backend

A pasta `backend` é separada. Ela não é necessária para gerar ou instalar o APK de teste.

O cliente Retrofit está preparado no projeto, mas o MVP atual não depende do backend para funcionar.

## 6. Se o Gradle reclamar

Verifique:

- Android Studio atualizado;
- Android SDK 35 instalado;
- JDK 17 selecionado;
- conexão com a internet na primeira sincronização, para baixar dependências;
- selecione a configuração `app` antes de executar.

## 7. Teste recomendado antes de enviar

1. Abrir o app.
2. Criar uma matéria.
3. Criar uma avaliação vinculada à matéria.
4. Escolher uma data e conferir se o prazo aparece corretamente.
5. Testar os filtros por matéria e status.
6. Marcar uma avaliação como concluída e depois reabri-la.
7. Fechar e abrir o app novamente.
8. Confirmar que os dados continuam salvos.
9. Conferir a ordem de prioridade no início.
10. Aceitar a permissão de notificações no Android 13 ou superior.

> Observação: esta é uma build de teste (`debug`), adequada para compartilhar com colegas e validar o MVP. Para distribuição oficial, deve ser criada uma build `release` assinada.
