git init
git branch -M main
git config user.name "Seu Nome"
git config user.email "seu@email.com"

git add settings.gradle.kts build.gradle.kts gradle.properties app/src/main/AndroidManifest.xml app/build.gradle.kts
git commit -m "chore: cria projeto Android Compose"

git add app/src/main/java/br/com/studyflow/ui/StudyFlowApp.kt
git commit -m "feat: cria dashboard inicial"

git add app/src/main/java/br/com/studyflow/data/model/Models.kt app/src/main/java/br/com/studyflow/ui/StudyViewModel.kt
git commit -m "feat: adiciona materias e avaliacoes"

git add app/src/main/java/br/com/studyflow/data
git commit -m "feat: adiciona Room para persistencia local"

git add app/src/main/java/br/com/studyflow/Priority.kt
git commit -m "feat: implementa prioridade 60 40"

git add app/src/main/java/br/com/studyflow/network
git commit -m "feat: adiciona cliente Retrofit"

git add backend
git commit -m "feat: cria API Node Express SQLite"

git add app/src/test
git commit -m "test: adiciona teste da prioridade"

git add README.md docs scripts
git commit -m "docs: adiciona documentacao e roteiro de commits"

Write-Host "Historico criado. Agora adicione o remote e faca push."
